package com.whotw.uaa.config;

import com.whotw.security.annotation.EnableWhotwResourceServer;
import com.whotw.security.domain.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器配置
 *
 * @author EdisonXu
 * @date 2019/7/12
 */
@Configuration(SecurityConstants.RESOURCE_SERVER_CONFIGURER)
@EnableWhotwResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${whotw.security.permission-check.exclude-url-patterns:}")
    private String[] excludeUrlPatterns;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(excludeUrlPatterns)
                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                //凡需要使用OAuth2校验的资源URL，需要在这里配置，否则走的是默认的用户密码校验
                .requestMatchers()
                    .antMatchers("/user/**", "/clients/**", "/api/**")
                    .and()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
        ;
    }
}
