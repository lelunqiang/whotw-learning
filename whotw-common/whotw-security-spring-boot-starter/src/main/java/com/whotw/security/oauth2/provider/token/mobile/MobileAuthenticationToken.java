package com.whotw.security.oauth2.provider.token.mobile;

import com.whotw.security.core.AccountDetails;
import com.whotw.security.oauth2.provider.token.CustomAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 根据手机号验证账户信息的Token
 *
 * @author EdisonXu
 * @date 2019-07-24
 */

public class MobileAuthenticationToken extends AbstractAuthenticationToken implements CustomAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private Object credentials;
    private String institutionId;
    private AccountDetails availableAccount;
    private boolean isAdminLogin;

    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public MobileAuthenticationToken(Object principal, Object credentials, String institutionId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
        setAuthenticated(false);
    }

    public MobileAuthenticationToken(Object principal, Object credentials, String institutionId, boolean isAdminLogin) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
        this.isAdminLogin = isAdminLogin;
        setAuthenticated(false);
    }

    public MobileAuthenticationToken(Object principal, Object credentials,
                                     AccountDetails availableAccount,
                                     String institutionId,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.institutionId = institutionId;
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

    public boolean isAdminLogin() {
        return isAdminLogin;
    }

    public MobileAuthenticationToken setAdminLogin(boolean adminLogin) {
        isAdminLogin = adminLogin;
        return this;
    }
}
