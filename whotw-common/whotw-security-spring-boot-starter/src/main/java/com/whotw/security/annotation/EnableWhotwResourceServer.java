package com.whotw.security.annotation;

import com.whotw.security.component.SecurityBeanDefinitionRegister;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * 资源服务器注解
 *
 * @author EdisonXu
 * @date 2019-07-22
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityBeanDefinitionRegister.class)
public @interface EnableWhotwResourceServer {
}
