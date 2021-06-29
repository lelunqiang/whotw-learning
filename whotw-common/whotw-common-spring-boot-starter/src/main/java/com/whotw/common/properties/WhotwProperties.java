package com.whotw.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Properties specific to JHipster.
 *
 * <p> Properties are configured in the application.yml file. </p>
 * <p> This class also load properties in the Spring Environment from the git.properties and META-INF/build-info.properties
 * files if they are found in the classpath.</p>
 */
@ConfigurationProperties(prefix = "whotw", ignoreUnknownFields = true)
@PropertySources({
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
})
@RefreshScope
public class WhotwProperties {

    private final JwtProperties jwt = new JwtProperties();

    private final ValidationCodeProperties validationCodeProperties = new ValidationCodeProperties();

    public ValidationCodeProperties getValidationCode(){
        return validationCodeProperties;
    }

    public JwtProperties getJwt() {
        return jwt;
    }


}
