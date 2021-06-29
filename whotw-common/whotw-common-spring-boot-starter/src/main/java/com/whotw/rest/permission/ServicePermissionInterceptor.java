package com.whotw.rest.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.security.permission.PermissionValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.whotw.security.domain.SecurityConstants;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

/**
 * @author EdisonXu
 * @date 2020-02-06
 */
//TODO: 暂不启用该类
//@Component
public class ServicePermissionInterceptor extends PermissionValidator implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServicePermissionInterceptor.class);

    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    private Long permissionPos;
    private Long permissionIdx;
    private Long[] permission;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String instServicePermissionSum = request.getHeader(SecurityConstants.HEADER_KEY_INST_PERMISSION);
        String innerAccess = request.getHeader(SecurityConstants.HEADER_KEY_INNER_ACCESS);

        // 如果是内部服务之间的调用，无需检测权限
        if(StringUtils.isNotBlank(innerAccess))
            return true;

        //根据头中的权限字符串，解析用户的权限信息
        List<Map<Long, Long>> instServicePermissions = parsePermissionMap(instServicePermissionSum, true);

        boolean permissionAllowed = doPermissionCheck(permission, instServicePermissions, true);
        if(logger.isDebugEnabled()){
            logger.debug("当前访问地址：[{}]{} ，权限校验{}", request.getMethod(), request.getRequestURI(), permissionAllowed?"成功":"失败");
        }
        if(!permissionAllowed)
            throw new AccessDeniedException("无权限访问");
        return true;
    }


}
