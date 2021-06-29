package com.whotw.common.config;

import com.whotw.common.data.ResourceEndpoint;
import com.whotw.rest.permission.PermissionFeignClient;
import com.whotw.utils.ProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

/**
 * @author EdisonXu
 * @date 2019-10-18
 */
//TODO: 暂不启用
//@Order(2)
//@Component
//@ConditionalOnExpression("${whotw.security.permission-check.enabled:true} && !${whotw.security.permission-check.local:false} ")
//@DependsOn("permissionInterceptor")
public class ServicePermissionInitializer //implements ApplicationRunner
{

    private static final Logger logger = LoggerFactory.getLogger(ServicePermissionInitializer.class);

    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private ProfileUtil profileUtil;

    public ServicePermissionInitializer() {
        logger.info("启用远程服务权限初始化器");
    }

    //@Override
    public void run(ApplicationArguments args) {
        final String applicationName = profileUtil.getApplicationName();

        try {
            logger.debug("向UAA申请权限");
            Long[] permission = permissionFeignClient.register(
                "功能服务", "POST", applicationName, ResourceEndpoint.TYPE_SERVICE
            );
            //TODO: 将permission设置到ServicePermissionInterceptor中
            logger.info("服务权限信息获取成功: [{},{}]", permission[0], permission[1]);
        } catch (Exception e) {
            logger.error("注册服务资源点失败", e);
        }

    }
}
