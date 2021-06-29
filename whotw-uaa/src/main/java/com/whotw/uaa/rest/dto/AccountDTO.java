package com.whotw.uaa.rest.dto;

import com.whotw.uaa.entity.AccountEntity;
import com.whotw.uaa.entity.AccountRoleEntity;

import java.util.List;

/**
 * @author EdisonXu
 * @date 2019-10-22
 */
public class AccountDTO extends AccountEntity {

    private static final long serialVersionUID = 1748197532294642118L;

    private List<AccountRoleEntity> applicationRoles;
    private boolean newCreateInstitution = false;
    private boolean parentLogin = false;
    private Boolean isNew;

    public List<AccountRoleEntity> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(List<AccountRoleEntity> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public boolean isNewCreateInstitution() {
        return newCreateInstitution;
    }

    public void setNewCreateInstitution(boolean newCreateInstitution) {
        this.newCreateInstitution = newCreateInstitution;
    }

    public boolean isParentLogin() {
        return parentLogin;
    }

    public AccountDTO setParentLogin(boolean parentLogin) {
        this.parentLogin = parentLogin;
        return this;
    }

    public Boolean getNew() {
        return isNew;
    }

    public AccountDTO setNew(Boolean aNew) {
        isNew = aNew;
        return this;
    }
}
