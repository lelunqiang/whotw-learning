package com.whotw.distribute.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author EdisonXu
 * @date 2019/7/11
 */
@Configuration
@EnableConfigurationProperties({DistributeProperties.class})
public class ConfigurationConfig {
}
