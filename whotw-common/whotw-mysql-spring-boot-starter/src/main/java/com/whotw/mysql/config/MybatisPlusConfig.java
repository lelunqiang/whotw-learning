package com.whotw.mysql.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.whotw.mysql.audit.AuditMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.whotw.security.domain.audit.Operator;


/**
 * @author EdisonXu
 * @date 2019-09-04
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.whotw.*.mapper.*", "com.whotw.mapper.*"})
public class MybatisPlusConfig {

    @Autowired(required = false)
    private AuditorAware<Operator> auditorAware;

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 自动填充审计字段
     *
     * @return AuditMetaObjectHandler
     */
    @Bean
    @ConditionalOnMissingBean(name = "auditMetaObjectHandler")
    public AuditMetaObjectHandler auditMetaObjectHandler() {
        return new AuditMetaObjectHandler(auditorAware);
    }

}
