package com.whotw.common.config;

import com.whotw.common.data.ResourceEndpoint;
import com.whotw.rest.permission.PermissionFeignClient;
import com.whotw.rest.permission.PermissionInterceptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-10-18
 */
@Order(2)
@Component
@ConditionalOnExpression("${whotw.security.permission-check.enabled:true} && !${whotw.security.permission-check.local:false} ")
@DependsOn("permissionInterceptor")
public class PermissionInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInitializer.class);

    @Value("${whotw.security.permission-check.url-patterns:/api/**,/management/**}")
    private String[] urlPatterns;
    @Value("${whotw.security.permission-check.exclude-url-patterns:}")
    private String[] excludeUrlPatterns;
    @Value("#{new Boolean('${whotw.security.service-permission-check.enabled:false}')}")
    private boolean enableServicePermissionCheck;
    @Value("${info.project.description}")
    private String serviceDescription;
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private Environment env;

    public PermissionInitializer() {
        logger.info("??????????????????????????????");
    }

    @Override
    public void run(ApplicationArguments args){
       final List<ResourceEndpoint> resourceEndpoints = new ArrayList<>();
       final Map<String, RequestMappingInfo> urlMapping = new HashMap<>();
       requestMappingHandlerMapping.getHandlerMethods().forEach((i,m)->{
           //???????????????Pattern??????REST??????
           List<String> urls = i.getPatternsCondition()
                   .getPatterns()
                   .stream()
                   .filter(p-> PatternMatchUtils.simpleMatch(urlPatterns, p))
                   .filter(p-> !PatternMatchUtils.simpleMatch(excludeUrlPatterns, p))
                   .collect(Collectors.toList());
           if(CollectionUtils.isEmpty(urls))
               return;
           int count = 0;
           Api api = m.getBeanType().getAnnotation(Api.class);
           String title = api!=null&&api.tags()!=null&& StringUtils.isNotBlank(api.tags()[0]) ? api.tags()[0] : null;
           for (RequestMethod method : i.getMethodsCondition().getMethods()) {
               // ??????Swagger????????????????????????
               ApiOperation apiOperation = m.getMethod().getAnnotation(ApiOperation.class);

               String methodName = method.name();
               String url = urls.get(count);
               String description = apiOperation!=null && apiOperation.value()!=null ? apiOperation.value() : "";
               description = StringUtils.isNotBlank(title) ? title+":"+description : description;

               resourceEndpoints.add(new ResourceEndpoint(url, methodName, description));
               urlMapping.put(buildKey(methodName, url), i);
               count++;
           }
       });

        //????????????????????????????????????????????????????????????????????????????????????
        if(enableServicePermissionCheck)
            resourceEndpoints.add(new ResourceEndpoint(getApplicationName(), "/", serviceDescription, ResourceEndpoint.TYPE_SERVICE));

        try {
            logger.debug("???UAA????????????");
            List<ResourceEndpoint> resourceEndpointsRegistered = permissionFeignClient.batchRegister(resourceEndpoints);
            logger.debug("????????????");
            for (ResourceEndpoint resourceEndpoint : resourceEndpointsRegistered){
                Long permissionPos = resourceEndpoint.getPermissionPos();
                Long permissionIdx = resourceEndpoint.getPermissionIdx();
                Long[] permission = {permissionPos, permissionIdx};
                logger.info("?????????[{}]{},{} ????????????????????????", resourceEndpoint.getMethod(), resourceEndpoint.getUrl(), resourceEndpoint.getDescription());
                if (logger.isDebugEnabled()) {
                    logger.debug("?????????[{}]{}, {} ?????????[{},{}]", resourceEndpoint.getMethod(), resourceEndpoint.getUrl(), resourceEndpoint.getDescription(), permissionPos, permissionIdx);
                }
                RequestMappingInfo mappingInfo = urlMapping.get(buildKey(resourceEndpoint.getMethod(), resourceEndpoint.getUrl()));
                if(mappingInfo==null)
                    logger.error("URL{}????????????mapping", resourceEndpoint.getUrl());
                else
                    permissionInterceptor.addResourceEndpointPermission(mappingInfo, permission);
            }
        } catch (Exception e) {
            logger.error("?????????????????????", e);
        }

    }

    private String buildKey(String methodName, String url){
        return methodName + ":" +url;
    }

    public String getApplicationName(){
        return env.getProperty("spring.application.name");
    }
}
