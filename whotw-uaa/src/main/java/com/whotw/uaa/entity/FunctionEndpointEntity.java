package com.whotw.uaa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 系统功能点Entity
 * </p>
 *
 * @author edison
 * @since 2019-10-18
 */
@ApiModel("系统功能点Entity")
@TableName("function_endpoints")
@KeySequence("distributeKeyGenerator")
public class FunctionEndpointEntity extends Model<FunctionEndpointEntity> implements Serializable{

    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    @Transient
    private static final Logger logger = LoggerFactory.getLogger(FunctionEndpointEntity.class);

    /**
     * 功能点ID
     */
    @ApiModelProperty("功能点ID")
    @TableId(type = IdType.ID_WORKER)
    protected Long id;
    /**
     * 功能点名称
     */
    @ApiModelProperty("功能点名称")
    @NotEmpty(message = "必须提供功能点名称")
    protected String name;
    /**
     * 父功能点ID
     */
    @ApiModelProperty("父功能点ID")
    protected Long parentId;
    /**
     * 对应根节点功能点ID
     */
    @ApiModelProperty("对应根节点功能点ID")
    protected Long rootId;
    /**
     * 该功能点绑定的资源点ID汇总，以,分隔
     */
    @ApiModelProperty("该功能点绑定的资源点ID汇总，以,分隔")
    protected String resourcesBound;
    /**
     * 资源点权限集合
     */
    @ApiModelProperty("资源点权限集合")
    protected String resourcesPermissionSum;
    /**
     * 资源点权限为方便查询的Map形式（不存储于数据库，仅用于方便判断权限）
     */
    @Transient
    @TableField(exist = false)
    @ApiModelProperty("资源点权限为方便查询的Map形式")
    protected Map<Long, Long> resourcePermissionSumValue;
    /**
     * 用户是否可自定义
     */
    @ApiModelProperty("用户能否自定义")
    protected boolean customizable;

    @TableField(fill = FieldFill.INSERT)
    protected String createdBy;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    @TableField(fill = FieldFill.UPDATE)
    protected String lastUpdatedBy;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime lastUpdatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getResourcesPermissionSum() {
        return resourcesPermissionSum;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public void setResourcesPermissionSum(String resourcesPermissionSum) {
        this.resourcesPermissionSum = resourcesPermissionSum;
        if(StringUtils.isNotBlank(resourcesPermissionSum)){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, Long.class, Long.class);
                resourcePermissionSumValue = objectMapper.readValue(resourcesPermissionSum.getBytes(), type);
            } catch (IOException e) {
                logger.error("无法解析权限集！");
            }
        }
    }

    public String getResourcesBound() {
        return resourcesBound;
    }

    public void setResourcesBound(String resourcesBound) {
        this.resourcesBound = resourcesBound;
    }

    public Map<Long, Long> getResourcePermissionSumValue() {
        return resourcePermissionSumValue;
    }

    public void setResourcePermissionSumValue(Map<Long, Long> resourcePermissionSumValue) {
        this.resourcePermissionSumValue = resourcePermissionSumValue;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    public void buildAndSetResourcePermissionSum(Map<Long, Long> resourcePermissionSumValue) throws JsonProcessingException {
        if(MapUtils.isEmpty(resourcePermissionSumValue))
            return;
        this.resourcePermissionSumValue = resourcePermissionSumValue;
        this.resourcesPermissionSum = new ObjectMapper().writeValueAsString(resourcePermissionSumValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionEndpointEntity that = (FunctionEndpointEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(rootId, that.rootId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId, rootId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("parentId", parentId)
                .append("rootId", rootId)
                .append("resourcesBound", resourcesBound)
                .append("resourcesPermissionSum", resourcesPermissionSum)
                .append("resourcePermissionSumValue", resourcePermissionSumValue)
                .append("customizable", customizable)
                .append("createdBy", createdBy)
                .append("createdTime", createdTime)
                .append("lastUpdatedBy", lastUpdatedBy)
                .append("lastUpdatedTime", lastUpdatedTime)
                .toString();
    }
}
