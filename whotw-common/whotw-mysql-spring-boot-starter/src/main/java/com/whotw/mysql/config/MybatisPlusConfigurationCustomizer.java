package com.whotw.mysql.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.whotw.mysql.custom.CustomMybatisXMLLanguageDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
@Component
public class MybatisPlusConfigurationCustomizer implements ConfigurationCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(MybatisPlusConfigurationCustomizer.class);

    @Override
    public void customize(MybatisConfiguration configuration) {
        logger.debug("使用自定义的ID生成器覆盖MybatisPlus");
        configuration.getLanguageRegistry().setDefaultDriverClass(CustomMybatisXMLLanguageDriver.class);
    }
}
