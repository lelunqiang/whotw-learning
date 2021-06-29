package com.whotw.uaa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.whotw.common.data.PermissionBitSpace;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 系统资源点Entity
 *
 * @author EdisonXu
 * @date 2019-10-18
 */
@ApiModel("资源点")
@TableName("resource_endpoints")
@KeySequence("distributeKeyGenerator")
public class ResourceEndpointEntity extends Model<ResourceEndpointEntity> implements PermissionBitSpace.PermissionBit {
    private static final long serialVersionUID = 1880721002931925646L;

    // API资源
    public static final Integer TYPE_API = 0;
    // 页面资源
    public static final Integer TYPE_PAGE = 1;
    // 服务资源
    public static final Integer TYPE_SERVICE = 2;

    /**
     * 资源点ID
     */
    @ApiModelProperty("资源点ID")
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 资源类型：0-API；1-页面；2-组
     */
    @ApiModelProperty("资源类型：0-API；1-页面；2-服务")
    private Integer type = TYPE_API;
    /**
     * 资源点描述
     */
    @ApiModelProperty("资源点描述")
    private String description;
    /**
     * REST接口资源点的HTTP方法
     */
    @ApiModelProperty("资源点REST接口方法")
    private String method;
    /**
     * REST接口资源点的URL
     */
    @ApiModelProperty("REST接口资源点的URL")
    private String url;
    /**
     * 权限空间标号
     */
    @ApiModelProperty("权限空间标号")
    private Long permissionIdx;
    /**
     * 权限位
     */
    @ApiModelProperty("权限位")
    private Long permissionPos;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 是否允许匿名访问
     */
    private boolean allowAnonymous;

    public ResourceEndpointEntity() {
    }

    public ResourceEndpointEntity(String description, String method, String url, Integer type) {
        this.description = description;
        this.method = method;
        this.url = url;
        this.type = type;
        this.createdTime = LocalDateTime.now(ZoneId.systemDefault());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPermissionIdx() {
        return permissionIdx;
    }

    public void setPermissionIdx(Long permissionIdx) {
        this.permissionIdx = permissionIdx;
    }

    public Long getPermissionPos() {
        return permissionPos;
    }

    public void setPermissionPos(Long permissionPos) {
        this.permissionPos = permissionPos;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isAllowAnonymous() {
        return allowAnonymous;
    }

    public void setAllowAnonymous(boolean allowAnonymous) {
        this.allowAnonymous = allowAnonymous;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("description", description)
                .append("method", method)
                .append("url", url)
                .append("permissionIdx", permissionIdx)
                .append("permissionPos", permissionPos)
                .append("createdTime", createdTime)
                .toString();
    }
}
