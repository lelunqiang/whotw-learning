package com.whotw.common.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author EdisonXu
 * @date 2019-11-29
 */
public class ResourceEndpoint implements Serializable, PermissionBitSpace.PermissionBit {

    private static final long serialVersionUID = 2582951414709458405L;

    // API资源
    public static final Integer TYPE_API = 0;
    // 页面资源
    public static final Integer TYPE_PAGE = 1;
    // 服务资源
    public static final Integer TYPE_SERVICE = 2;

    /**
     * id
     */
    private Long id;
    /**
     * REST接口资源点的URL
     */
    private String url;
    /**
     * REST接口资源点的HTTP方法
     */
    private String method;
    /**
     * 资源点描述
     */
    private String description;
    /**
     * 权限空间标号
     */
    private Long permissionIdx;
    /**
     * 权限位
     */
    private Long permissionPos;
    /**
     * 资源类型
     */
    private Integer type = TYPE_API;

    public ResourceEndpoint() {
    }

    public ResourceEndpoint(String url, String method, String description) {
        this.url = url;
        this.method = method;
        this.description = description;
    }

    public ResourceEndpoint(String url, String method, String description, Integer type) {
        this.url = url;
        this.method = method;
        this.description = description;
        this.type = type;
    }

    public ResourceEndpoint(String url, Long permissionIdx, Long permissionPos) {
        this.url = url;
        this.permissionIdx = permissionIdx;
        this.permissionPos = permissionPos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("url", url)
                .append("method", method)
                .append("description", description)
                .append("permissionIdx", permissionIdx)
                .append("permissionPos", permissionPos)
                .append("type", type)
                .toString();
    }
}
