package com.whotw.rest.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.security.permission.PermissionValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.whotw.security.domain.SecurityConstants;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EdisonXu
 * @date 2019-10-18
 */
@Component
public class PermissionInterceptor extends PermissionValidator implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);
    //接口的资源权限缓存
    private Map<RequestMappingInfo, Long[]> resourceEndpointPermissionMap = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accountPermissionSum = request.getHeader(SecurityConstants.HEADER_KEY_ACCOUNT_PERMISSION);
        String innerAccess = request.getHeader(SecurityConstants.HEADER_KEY_INNER_ACCESS);

        // 如果是内部服务之间的调用，无需检测权限
        if(StringUtils.isNotBlank(innerAccess))
            return true;

        //根据头中的权限字符串，解析用户的权限信息
        List<Map<Long, Long>> userPermissions = parsePermissionMap(accountPermissionSum, true);

        for(Map.Entry<RequestMappingInfo, Long[]> entry : resourceEndpointPermissionMap.entrySet()){
            RequestMappingInfo requestMappingInfo = entry.getKey();
            Long[] resourceEndpointPermission = entry.getValue();
            RequestMappingInfo matched = requestMappingInfo.getMatchingCondition(request);
            if(matched==null) //未匹配上，可能未配置权限，默认允许访问
                continue;
            //匹配上则进行权限校验
            //logger.debug("当前访问地址：{} 匹配权限{}，进行权限校验", request.getRequestURI(), permission);
            boolean permissionAllowed = doPermissionCheck(resourceEndpointPermission, userPermissions, true);
            if(logger.isDebugEnabled()){
                logger.debug("当前访问地址：[{}]{} 匹配权限{}，权限校验{}", request.getMethod(), request.getRequestURI(), resourceEndpointPermission, permissionAllowed?"成功":"失败");
            }
            if(!permissionAllowed)
                throw new AccessDeniedException("无权限访问");
            if(permissionAllowed)
                return true;
        }
        logger.debug("当前访问地址：[{}]{} 未匹配上任何权限，默认放行", request.getMethod(), request.getRequestURI());
        return true;
    }

    /**
     * 将当前接口对应的权限信息（权限空间：权限位）加入到内存中
     * @param mappingInfo
     * @param resourceEndpointPermission
     */
    public void addResourceEndpointPermission(RequestMappingInfo mappingInfo, Long[] resourceEndpointPermission){
        resourceEndpointPermissionMap.put(mappingInfo, resourceEndpointPermission);
    }
}
