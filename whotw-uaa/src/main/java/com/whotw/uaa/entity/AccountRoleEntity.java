package com.whotw.uaa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色Entity
 * </p>
 *
 * @author edison
 * @since 2019-10-18
 */
@ApiModel("账号角色")
@TableName("account_role")
public class AccountRoleEntity extends Model<AccountRoleEntity> implements Serializable, Comparable<AccountRoleEntity> {

    private static final long serialVersionUID = 1L;

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

    @TableField(fill = FieldFill.INSERT)
    protected String createdBy;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    @TableField(fill = FieldFill.UPDATE)
    protected String lastUpdatedBy;

    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime lastUpdatedTime;

    public AccountRoleEntity() {
    }

    public AccountRoleEntity(Long id) {
        this.id = id;
    }

    public AccountRoleEntity(Long id,
                             @NotEmpty(message = "必须填写角色名称") String name,
                             @NotNull(message = "必须提供机构ID") Long institutionId,
                             @NotNull(message = "必须提供机构名称") String institutionName) {
        this.id = id;
        this.name = name;
        this.institutionId = institutionId;
        this.institutionName = institutionName;
    }

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

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @Override
    public String toString() {
        return "AccountRoleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", institutionId=" + institutionId +
                ", institutionName=" + institutionName +
                ", deptId=" + deptId +
                ", parentId=" + parentId +
                ", rootId=" + rootId +
                ", permissionSum='" + permissionSum + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }

    @Override
    public int compareTo(AccountRoleEntity o) {
        return this.id.compareTo(o.getId());
    }
}
