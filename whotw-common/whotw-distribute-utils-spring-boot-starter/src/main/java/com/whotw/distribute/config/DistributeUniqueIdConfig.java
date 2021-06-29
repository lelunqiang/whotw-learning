package com.whotw.distribute.config;

import com.whotw.common.data.DistributeUniqueId;
import com.whotw.common.data.SystemClock;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author EdisonXu
 * @date 2019-07-17
 */
@Configuration
public class DistributeUniqueIdConfig implements InitializingBean {

    private static final Logger logger = getLogger(DistributeUniqueIdConfig.class);

    @Resource
    private DistributeProperties distributeProperties;

    private DistributeUniqueId distributeUniqueId;

    @Override
    public void afterPropertiesSet() throws Exception {
        SystemClock.INSTANCE.initialize();
        logger.debug("Distribute properties: "+ distributeProperties);
        distributeUniqueId = new DistributeUniqueId(
                distributeProperties.getDataCenterId(),
                distributeProperties.getWorkerId(),
                distributeProperties.getOptimizeClock(),
                distributeProperties.getTimeOffset(),
                distributeProperties.getRandomSequence());
    }

    @Bean
    public DistributeUniqueId distributeUniqueId(){
        return distributeUniqueId;
    }
}
