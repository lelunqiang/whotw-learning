package com.whotw.security.core;

import com.whotw.security.domain.Account;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * 将必要的账户信息放到上下文中，方便随时获取和调用，其字段应该
 * 对应{@link com.whotw.security.domain.Account}
 *
 * @author EdisonXu
 * @date 2019-07-22
 */

public class AccountDetails implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private Account account;
    private List<Account> accountBound;
    private Set<GrantedAuthority> authorities;

    public AccountDetails(Account account) {
        this(account, null);
    }

    public AccountDetails(Account account, Set<GrantedAuthority> authorities) {
        Assert.notNull(account, "Account must be set");
        this.account = account;
        this.authorities = authorities;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                new AccountDetails.AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority,
                    "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    @Override
    public void eraseCredentials() {
        account.setPassword(null);
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>,
            Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set.
            // If the authority is null, it is a custom authority and should precede
            // others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    public void setUsername(String username) {
        this.account.setUsername(username);
    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    public void setPassword(String password) {
        this.account.setPassword(password);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.account.isExpired();
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.account.setExpired(!accountNonExpired);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.account.isLocked();
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.account.setLocked(!accountNonLocked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.account.isCredentialsExpired();
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.account.setCredentialsExpired(!credentialsNonExpired);
    }

    @Override
    public boolean isEnabled() {
        return this.account.isActivated();
    }

    public void setEnabled(boolean enabled) {
        this.account.setActivated(enabled);
    }

    public String getWxOpenId(){
        return this.account.getWxOpenId();
    }

    public String getWxUnionId(){
        return this.account.getWxUnionId();
    }

    public void setWxOpenId(String openId){
        this.account.setWxOpenId(openId);
    }

    public void setWxUnionId(String unionId){
        this.account.setWxUnionId(unionId);
    }

    public Long getAccountId(){
        return this.account.getId();
    }

    public String getWxSessionKey(){
        return this.account.getWxSessionKey();
    }

    public Long getUserId(){
        return this.account.getUserId();
    }

    public Account getAccount() {
        return account;
    }

    public List<Account> getAccountBound() {
        return accountBound;
    }

    public AccountDetails setAccountBound(List<Account> accountBound) {
        this.accountBound = accountBound;
        return this;
    }
}
