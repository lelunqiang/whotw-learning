package com.whotw.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * TokenStore的配置
 *
 * @author EdisonXu
 * @date 2019/7/13
 */
@Configuration
public class TokenStoreConfig {

    @Configuration
    @ConditionalOnProperty(prefix = "whotw.security.oauth2",name="token-store", havingValue = "memory", matchIfMissing = true)
    public static class InMemoryTokenStoreConfig{
        /**
         * @return
         */
        @Bean
        public TokenStore inMemoryTokenStore() {
            return new InMemoryTokenStore();
        }

    }

    @Configuration
    @ConditionalOnProperty(prefix = "whotw.security.oauth2",name="token-store", havingValue = "redis")
    public static class RedisTokenStoreConfig{
        @Autowired
        private RedisConnectionFactory redisConnectionFactory;

        /**
         * @return
         */
        @Bean
        public TokenStore redisTokenStore() {
            return new RedisTokenStore(redisConnectionFactory);
        }

    }

    @Configuration
    @ConditionalOnProperty(prefix = "whotw.security.oauth2",name="token-store", havingValue = "jwt")
    public static class JwtTokenStoreConfig{

        @Value("${whotw.security.oauth2.jwt-sign-key:@null}")
        private String signKey;

        @Bean
        public TokenStore jwtTokenStore(JwtAccessTokenConverter accessTokenConverter){
            return new JwtTokenStore(accessTokenConverter);
        }

        @Bean
        @ConditionalOnMissingBean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            //converter.setKeyPair(this.keyPair);
            if(StringUtils.isNotBlank(signKey))
                converter.setSigningKey(signKey);
            return converter;
        }
    }
}
