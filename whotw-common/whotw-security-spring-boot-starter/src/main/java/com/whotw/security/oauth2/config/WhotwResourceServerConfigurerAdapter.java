package com.whotw.security.oauth2.config;

import com.whotw.security.oauth2.provider.token.WhotwUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author EdisonXu
 * @date 2019-07-22
 */
public class WhotwResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

    @Autowired
    protected RemoteTokenServices remoteTokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                antMatcher("/actuator/**")
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                //凡需要使用OAuth2校验的资源URL，需要在这里配置，否则走的是默认的用户密码校验
                /*.antMatcher("/user/**")
                //.antMatcher("/clients/**")
                .authorizeRequests()
                .anyRequest()
                .authenticated()*/
                .requestMatchers()
                .antMatchers("/user/**", "/clients/**", "/api/**")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                    .disable()
        ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        WhotwUserAuthenticationConverter whotwUserAuthenticationConverter = new WhotwUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(whotwUserAuthenticationConverter);

        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        resources.tokenServices(remoteTokenServices);
    }
}
