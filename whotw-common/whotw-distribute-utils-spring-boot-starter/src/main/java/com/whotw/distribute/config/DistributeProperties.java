package com.whotw.distribute.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式信息配置
 *
 * @author EdisonXu
 * @date 2019-07-17
 */
@ConfigurationProperties(prefix = "whotw.sms")
public class DistributeProperties {

    private Long workerId;
    private Long dataCenterId;
    private Boolean optimizeClock;
    private Long timeOffset;
    private Boolean randomSequence;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Boolean getOptimizeClock() {
        return optimizeClock;
    }

    public void setOptimizeClock(Boolean optimizeClock) {
        this.optimizeClock = optimizeClock;
    }

    public Long getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(Long timeOffset) {
        this.timeOffset = timeOffset;
    }

    public Boolean getRandomSequence() {
        return randomSequence;
    }

    public void setRandomSequence(Boolean randomSequence) {
        this.randomSequence = randomSequence;
    }

    @Override
    public String toString() {
        return "Distribute{" +
                "workerId=" + workerId +
                ", dataCenterId=" + dataCenterId +
                ", optimizeClock=" + optimizeClock +
                ", timeOffset=" + timeOffset +
                ", randomSequence=" + randomSequence +
                '}';
    }
}
