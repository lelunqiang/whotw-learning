package com.whotw.security.component;

import com.whotw.security.domain.SecurityConstants;
import com.whotw.security.oauth2.config.WhotwResourceServerConfigurerAdapter;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author EdisonXu
 * @date 2019-07-22
 */

public class SecurityBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    private static final Logger logger = getLogger(SecurityBeanDefinitionRegister.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (registry.isBeanNameInUse(SecurityConstants.RESOURCE_SERVER_CONFIGURER)) {
            logger.warn("本地存在资源服务器配置，覆盖默认配置:" + SecurityConstants.RESOURCE_SERVER_CONFIGURER);
            return;
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(WhotwResourceServerConfigurerAdapter.class);
        registry.registerBeanDefinition(SecurityConstants.RESOURCE_SERVER_CONFIGURER, beanDefinition);
    }
}
