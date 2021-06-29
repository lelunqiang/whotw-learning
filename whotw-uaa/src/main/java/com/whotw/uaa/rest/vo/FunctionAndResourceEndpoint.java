package com.whotw.uaa.rest.vo;

import com.whotw.common.data.TreeNode;
import com.whotw.uaa.entity.FunctionEndpointEntity;
import com.whotw.uaa.entity.ResourceEndpointEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

/**
 * @author EdisonXu
 * @date 2019-10-18
 */
@ApiModel("功能点与资源点绑定关系")
public class FunctionAndResourceEndpoint extends FunctionEndpointEntity implements TreeNode<FunctionAndResourceEndpoint> {
    private static final long serialVersionUID = 1307652369463815854L;

    @ApiModelProperty("对应一组资源点")
    private List<ResourceEndpointEntity> resourceEndpoints;
    @ApiModelProperty("子功能点")
    private Set<FunctionAndResourceEndpoint> children;
    @ApiModelProperty("关联的角色ID")
    private Long roleId;

    public List<ResourceEndpointEntity> getResourceEndpoints() {
        return resourceEndpoints;
    }

    public void setResourceEndpoints(List<ResourceEndpointEntity> resourceEndpoints) {
        this.resourceEndpoints = resourceEndpoints;
    }

    public Set<FunctionAndResourceEndpoint> getChildren() {
        return children;
    }

    public void setChildren(Set<FunctionAndResourceEndpoint> children) {
        this.children = children;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "FunctionAndResourceEndpoint{" +
                "resourceEndpoints=" + resourceEndpoints +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", rootId=" + rootId +
                ", resourcesBound='" + resourcesBound + '\'' +
                ", resourcesPermissionSum='" + resourcesPermissionSum + '\'' +
                ", resourcePermissionSumValue=" + resourcePermissionSumValue +
                ", children=" + children +
                '}';
    }
}
