package com.whotw.mysql.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author EdisonXu
 * @date 2019-09-03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@KeySequence("distributeKeyGenerator")
public abstract class AbstractEntity<T extends AbstractEntity> extends Model implements Serializable {

    private static final long serialVersionUID = -8001758066217227830L;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    protected String createdBy;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    protected String lastUpdatedBy;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime lastUpdatedTime;

    public String getCreatedBy() {
        return createdBy;
    }

    public AbstractEntity<T> setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public AbstractEntity<T> setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public AbstractEntity<T> setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public AbstractEntity<T> setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }
}
