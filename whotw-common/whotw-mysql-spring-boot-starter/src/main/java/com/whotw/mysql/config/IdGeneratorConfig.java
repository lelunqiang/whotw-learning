package com.whotw.mysql.config;

import com.whotw.common.data.DistributeUniqueId;
import com.whotw.distribute.config.DistributeUniqueIdConfig;
import com.whotw.mysql.custom.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
@Configuration
@AutoConfigureAfter(DistributeUniqueIdConfig.class)
public class IdGeneratorConfig{
    private static final Logger logger = LoggerFactory.getLogger(IdGeneratorConfig.class);

    @Autowired
    private DistributeUniqueId distributeUniqueId;

    @PostConstruct
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public IdGenerator idGenerator(){
        logger.debug("初始化IdGenerator...");
        IdGenerator.instance.init(distributeUniqueId);
        return IdGenerator.instance;
    }
}
