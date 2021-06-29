package com.whotw.uaa.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author EdisonXu
 * @date 2019-07-16
 */

public class BaseEntity implements Serializable {

    @TableField
    @JsonIgnore
    private String createdBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDate createdDate;

    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    @JsonIgnore
    private Instant lastUpdateTime;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createTime) {
        this.createdTime = createTime;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Instant lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
