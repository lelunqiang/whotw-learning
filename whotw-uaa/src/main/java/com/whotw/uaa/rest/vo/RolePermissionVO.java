package com.whotw.uaa.rest.vo;

import com.whotw.common.data.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author EdisonXu
 * @date 2019-12-25
 */
@ApiModel("单一角色对应的权限")
public class RolePermissionVO implements Serializable, TreeNode<RolePermissionVO> {

    private static final long serialVersionUID = 8965223117636079646L;

    /**
     * 功能点ID
     */
    @ApiModelProperty("功能点ID")
    @NotNull(message = "必须提供功能点ID")
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
     * 是否拥有该功能点的权限
     */
    @ApiModelProperty("是否拥有该功能点的权限")
    private boolean hasPermission;
    /**
     * 子权限
     */
    @ApiModelProperty("子权限")
    private Set<RolePermissionVO> children;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public Set<RolePermissionVO> getChildren() {
        return children;
    }

    public void setChildren(Set<RolePermissionVO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("parentId", parentId)
                .append("rootId", rootId)
                .append("hasPermission", hasPermission)
                .append("children", children)
                .toString();
    }
}
