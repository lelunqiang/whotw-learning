package com.whotw.uaa.rest.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.whotw.common.data.TreeNode;
import com.whotw.uaa.entity.AccountRoleEntity;
import com.whotw.utils.CommonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author EdisonXu
 * @date 2019-10-20
 */
@ApiModel("账户角色")
public class AccountRoleVO implements Serializable, TreeNode<AccountRoleVO> {

    private static final long serialVersionUID = 8313648923957832373L;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", required = true)
    @NotEmpty(message = "必须填写角色名称")
    private String name;
    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID",required = true)
    @NotNull(message = "必须提供机构ID")
    private Long institutionId;
    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称",required = true)
    @NotNull(message = "必须提供机构名称")
    private String institutionName;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long deptId;
    /**
     * 父角色ID
     */
    @ApiModelProperty("父角色ID")
    private Long parentId;
    /**
     * 根节点角色ID
     */
    @ApiModelProperty("根节点ID")
    private Long rootId;
    /**
     * 权限集
     */
    @ApiModelProperty("权限集")
    private String permissionSum;
    /**
     *  角色对应的权限集
     */
    @ApiModelProperty("角色对应的权限集")
    private List<FunctionAndResourceEndpoint> functionEndpoints;
    /**
     * 子角色
     */
    @ApiModelProperty("子角色")
    private Set<AccountRoleVO> children;

    private String createdBy;

    private LocalDateTime createdTime;

    private String lastUpdatedBy;

    private LocalDateTime lastUpdatedTime;

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

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(String permissionSum) {
        this.permissionSum = permissionSum;
    }

    public List<FunctionAndResourceEndpoint> getFunctionEndpoints() {
        return functionEndpoints;
    }

    public void setFunctionEndpoints(List<FunctionAndResourceEndpoint> functionEndpoints) {
        this.functionEndpoints = functionEndpoints;
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

    public Set<AccountRoleVO> getChildren() {
        return children;
    }

    public void setChildren(Set<AccountRoleVO> children) {
        this.children = children;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public String toString() {
        return "AccountRoleVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", institutionId=" + institutionId +
                ", deptId=" + deptId +
                ", parentId=" + parentId +
                ", permissionSum='" + permissionSum + '\'' +
                ", functionEndpoints=" + functionEndpoints +
                '}';
    }

    public AccountRoleEntity toEntity() throws JsonProcessingException {
        AccountRoleEntity entity = CommonUtil.toEntity(this, AccountRoleEntity.class);

        return entity;
    }
}
