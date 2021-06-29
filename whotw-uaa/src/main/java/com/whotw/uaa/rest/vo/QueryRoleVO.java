package com.whotw.uaa.rest.vo;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * @author EdisonXu
 * @date 2019-10-20
 */
@ApiModel("查询角色条件封装")
public class QueryRoleVO {

    private Long institutionId;
    private String institutionName;
    private Long deptId;
    private Long parentId;
    private List<Long> functionEndpointIds;
    private String roleNameQueryStr;

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
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

    public List<Long> getFunctionEndpointIds() {
        return functionEndpointIds;
    }

    public void setFunctionEndpointIds(List<Long> functionEndpointIds) {
        this.functionEndpointIds = functionEndpointIds;
    }

    public String getRoleNameQueryStr() {
        return roleNameQueryStr;
    }

    public void setRoleNameQueryStr(String roleNameQueryStr) {
        this.roleNameQueryStr = roleNameQueryStr;
    }

    @Override
    public String toString() {
        return "QueryRoleVO{" +
                "institutionId=" + institutionId +
                ", deptId=" + deptId +
                ", parentId=" + parentId +
                ", functionEndpointIds=" + functionEndpointIds +
                ", roleNameQueryStr='" + roleNameQueryStr + '\'' +
                '}';
    }
}
