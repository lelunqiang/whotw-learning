package com.whotw.common.config;

import com.whotw.rest.permission.PermissionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 加载权限校验拦截器以及申请
 *
 * @author EdisonXu
 * @date 2019-10-18
 */
@Configuration
@ConditionalOnProperty(prefix = "whotw.security.permission-check", name="enabled", havingValue = "true", matchIfMissing=true)
public class PermissionInterceptorConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInterceptorConfig.class);

    /*@Autowired
    private ServicePermissionInterceptor servicePermissionInterceptor;*/
    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Value("${whotw.security.permission-check.url-patterns:/api/**}")
    private String[] urlPatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        logger.info("启用权限拦截器");
        /*registry.addInterceptor(servicePermissionInterceptor)
                .addPathPatterns(urlPatterns);*/
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns(urlPatterns);
    }
}
