package com.whotw.uaa.entity;

import com.whotw.mysql.entity.AbstractEntity;

/**
 * @author EdisonXu
 * @date 2019-10-20
 */
public class RoleFunctionBinding extends AbstractEntity<RoleFunctionBinding> {

    private static final long serialVersionUID = 8481156220730161026L;

    private Long roleId;
    private Long functionEndpointId;

    public RoleFunctionBinding() {
    }

    public RoleFunctionBinding(Long roleId, Long functionEndpointId) {
        this.roleId = roleId;
        this.functionEndpointId = functionEndpointId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFunctionEndpointId() {
        return functionEndpointId;
    }

    public void setFunctionEndpointId(Long functionEndpointId) {
        this.functionEndpointId = functionEndpointId;
    }
}
