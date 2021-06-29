package com.whotw.common.config;

import com.whotw.common.properties.WhotwProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author EdisonXu
 * @date 2019-09-04
 */
@Configuration
@EnableConfigurationProperties({WhotwProperties.class})
@RefreshScope
public class PropertiesConfiguration {
}
