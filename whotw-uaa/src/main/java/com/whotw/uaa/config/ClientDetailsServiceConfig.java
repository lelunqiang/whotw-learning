package com.whotw.uaa.config;

import com.whotw.uaa.security.service.CacheableJdbcClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 配置ClientDetailsService
 *
 * @author EdisonXu
 * @date 2019/7/14
 */
@Configuration
public class ClientDetailsServiceConfig {

    @Resource
    private DataSource dataSource;

    @Bean("customClientDetailsService")
    //@Profile(ProfileConstants.SPRING_PROFILE_PRODUCTION)
    public ClientDetailsService cachedJdbcClientDetailsService(){
        return new CacheableJdbcClientDetailsService(dataSource);
    }

    /*@Bean("customClientDetailsService")
    @Profile(ProfileConstants.SPRING_PROFILE_DEVELOPMENT)
    public ClientDetailsService inMemoryClientDetailsService() throws Exception {
        ClientDetailsServiceBuilder builder = new ClientDetailsServiceBuilder();
        return builder
                .inMemory()
                .withClient("wx103178648d20e85c")
                .secret("{noop}189cf23694180ef2adb3bfbb4430b35c")
                //.redirectUris("http://localhost:9001/callback")
                .redirectUris("https://www.getpostman.com/oauth2/callback")
                .scopes("all")
                .authorizedGrantTypes("authorization_code", "implicit", "refresh_token")
                .and()
                .withClient("client2")
                .secret("{noop}1234")
                .scopes("all")
                .authorizedGrantTypes("password", "client_credentials", "refresh_token")
                .and()
                .withClient("test")
                .secret("{noop}1234")
                //.redirectUris("http://localhost:9001/callback")
                .redirectUris("https://www.getpostman.com/oauth2/callback")
                .scopes("all")
                .authorizedGrantTypes("authorization_code", "implicit", "refresh_token")
                .and()
            .build()
        ;
    }*/

}
