package com.whotw.uaa.config;

import com.whotw.rest.permission.PermissionInterceptor;
import com.whotw.uaa.security.service.ResourceEndpointService;
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
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * 注册资源点权限位到数据库同时将权限位放入权限拦截器，每个服务的权限点都会添加到权限拦截器的本地内存中
 * @date 2019-10-22
 */
@Component
@ConditionalOnExpression("${whotw.security.permission-check.enabled:true} && ${whotw.security.permission-check.local:false} ")
@DependsOn({"permissionInterceptor", "resourceEndpointService", "uaaInitializeService"})
public class LocalPermissionInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LocalPermissionInitializer.class);

    @Value("${whotw.security.permission-check.url-patterns:/api/**,/management/**}")
    private String[] urlPatterns;
    @Value("${whotw.security.permission-check.exclude-url-patterns:}")
    private String[] excludeUrlPatterns;
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private ResourceEndpointService resourceEndpointService;

    public LocalPermissionInitializer() {
        logger.info("启用本地权限初始化器");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        requestMappingHandlerMapping.getHandlerMethods().forEach((i,m)->{
            //根据指定的Pattern过滤REST接口
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
                // 获取Swagger中定义的接口描述
                ApiOperation apiOperation = m.getMethod().getAnnotation(ApiOperation.class);

                String methodName = method.name();
                String url = urls.get(count);
                String description = apiOperation!=null && apiOperation.value()!=null ? apiOperation.value() : "";
                description = StringUtils.isNotBlank(title) ? title+":"+description : description;

                try {
                    Long[] permission = resourceEndpointService.register(description, methodName, url);
                    Long permissionPos = permission != null && permission.length == 2 ? permission[0] : null;
                    Long permissionIdx = permission != null && permission.length == 2 ? permission[1] : null;
                    if (logger.isDebugEnabled()) {
                        if (apiOperation != null)
                            logger.debug("资源点[{}]{}, {} 权限位[{},{}]", methodName, url, description, permissionPos, permissionIdx);
                        else
                            logger.debug("[{}]{} 权限位[{},{}]", methodName, url, permissionPos, permissionIdx);
                    }
                    permissionInterceptor.addResourceEndpointPermission(i, permission);
                    logger.info("资源点[{}]{},{} 权限信息获取成功", methodName, url, description);
                } catch (Exception e) {
                    logger.error("注册资源点[{}]{}, {} 失败", methodName, url, description, e);
                } finally {
                    count++;
                }
            }
        });
    }
}
