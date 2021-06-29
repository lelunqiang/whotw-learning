package com.whotw.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author EdisonXu
 * @date 2019-07-26
 */
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityConfig {
}
