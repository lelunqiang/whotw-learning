package com.whotw.uaa.config;

import com.whotw.uaa.security.oauth2.provider.token.store.WhotwAccessTokenConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author EdisonXu
 * @date 2019-08-08
 */
@Configuration
public class AccessTokenConverterConfig {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenConverterConfig.class);

    @Value("${whotw.security.oauth2.jwt-sign-key:#{null}}")
    private String signKey;

    @Bean("whotwAccessTokenConverter")
    @ConditionalOnProperty(prefix = "whotw.security.oauth2",name="token-store", havingValue = "jwt")
    public WhotwAccessTokenConverter jwtAccessTokenConverter(){
        logger.debug("使用提供JWT支持的AccessTokenConverter");
        WhotwAccessTokenConverter whotwAccessTokenConverter = new WhotwAccessTokenConverter(true);
        if(StringUtils.isNotBlank(signKey))
            whotwAccessTokenConverter.setSigningKey(signKey);
        else
            logger.warn("未指定JWT的签名密钥！默认将使用随机数作为密钥。");
        return whotwAccessTokenConverter;
    }

    @Bean
    @ConditionalOnMissingBean
    public WhotwAccessTokenConverter whotwAccessTokenConverter(){
        logger.debug("使用默认的AccessTokenConverter");
        return new WhotwAccessTokenConverter();
    }

}
