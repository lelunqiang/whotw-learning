package com.whotw.mysql.audit;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.data.domain.AuditorAware;

import com.whotw.security.domain.audit.Operator;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {

    private AuditorAware<Operator> auditorAware;

    public AuditMetaObjectHandler(AuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasSetter("createdTime"))
            this.setFieldValByName("createdTime", LocalDateTime.now(ZoneId.systemDefault()), metaObject);
        if(metaObject.hasSetter("createdBy")&& auditorAware !=null && auditorAware.getCurrentAuditor()!=null)
            auditorAware.getCurrentAuditor().ifPresent(operator->this.setFieldValByName("createdBy", operator.getFullName(), metaObject));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("lastUpdatedTime"))
            this.setFieldValByName("lastUpdatedTime", LocalDateTime.now(ZoneId.systemDefault()), metaObject);
        if(metaObject.hasSetter("lastUpdatedBy")&& auditorAware !=null && auditorAware.getCurrentAuditor()!=null)
            auditorAware.getCurrentAuditor().ifPresent(operator->this.setFieldValByName("lastUpdatedBy", operator.getFullName(), metaObject));
    }
}
