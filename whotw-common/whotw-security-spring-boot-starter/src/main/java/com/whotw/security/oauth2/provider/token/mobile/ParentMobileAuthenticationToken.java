package com.whotw.security.oauth2.provider.token.mobile;

import com.whotw.security.core.AccountDetails;
import com.whotw.security.oauth2.provider.token.CustomAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 根据家长手机号验证账户信息的Token
 *
 * @author EdisonXu
 * @date 2019-07-24
 */

public class ParentMobileAuthenticationToken extends AbstractAuthenticationToken implements CustomAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private Object credentials;
    private String institutionId;
    private Long parentBindId;
    private AccountDetails availableAccount;

    public ParentMobileAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public ParentMobileAuthenticationToken(Object principal, Object credentials, String institutionId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
        setAuthenticated(false);
    }

    public ParentMobileAuthenticationToken(Object principal, Object credentials, String institutionId, Long parentBindId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
        this.parentBindId = parentBindId;
        setAuthenticated(false);
    }

    public ParentMobileAuthenticationToken(Object principal, Object credentials,
                                           AccountDetails availableAccount,
                                           String institutionId,
                                            Long parentBindId,
                                           Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
        this.parentBindId = parentBindId;
        this.availableAccount = availableAccount;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    @Override
    public AccountDetails getAccountDetails() {
        return availableAccount;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.availableAccount=availableAccount;
    }

    public Long getParentBindId() {
        return parentBindId;
    }

    public ParentMobileAuthenticationToken setParentBindId(Long parentBindId) {
        this.parentBindId = parentBindId;
        return this;
    }
}
