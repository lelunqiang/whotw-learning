package com.whotw.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whotw.common.data.SystemRole;
import com.whotw.security.domain.Account.AccountType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 对应{@link com.whotw.security.domain.Account} 的Entity
 *
 * @author EdisonXu
 * @date 2019-07-16
 */
@TableName("whotw_account")
public class AccountEntity extends BaseEntity{

    private static final long serialVersionUID = -6301962007749903512L;

    @TableId
    private Long id;

    private Long userId;

    private Long institutionId;

    private String institutionName;

    private String institutionLogo;

    private String username;

    private String mobile;

    private String password;

    private String userFullName;

    private String email;

    private String avatarUrl;

    private String wxOpenId;
    private String wxWpOpenId;

    private String wxUnionId;

    private String institutionAddress;

    private SystemRole systemRole;

    private Boolean skipIntro = false;

    private AccountType accountType = AccountType.TRIAL;

    private boolean expired;

    private boolean activated = true;

    private LocalDateTime expireTime;

    private boolean isAdmin;

    public String getWxWpOpenId() {
        return wxWpOpenId;
    }

    public AccountEntity setWxWpOpenId(String wxWpOpenId) {
        this.wxWpOpenId = wxWpOpenId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public Boolean getSkipIntro() {
        return skipIntro;
    }

    public void setSkipIntro(Boolean skipIntro) {
        this.skipIntro = skipIntro;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }

    public String getInstitutionAddress() {
        return institutionAddress;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public AccountEntity setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    public String getInstitutionLogo() {
        return institutionLogo;
    }

    public AccountEntity setInstitutionLogo(String institutionLogo) {
        this.institutionLogo = institutionLogo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity entity = (AccountEntity) o;
        return expired == entity.expired &&
                activated == entity.activated &&
                isAdmin == entity.isAdmin &&
                Objects.equals(id, entity.id) &&
                Objects.equals(userId, entity.userId) &&
                Objects.equals(institutionId, entity.institutionId) &&
                Objects.equals(username, entity.username) &&
                Objects.equals(mobile, entity.mobile) &&
                Objects.equals(userFullName, entity.userFullName) &&
                Objects.equals(email, entity.email) &&
                systemRole == entity.systemRole &&
                accountType == entity.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, institutionId, username, mobile, userFullName, email, systemRole, accountType, expired, activated, isAdmin);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("userId", userId)
                .append("institutionId", institutionId)
                .append("institutionName", institutionName)
                .append("institutionLogo", institutionLogo)
                .append("username", username)
                .append("mobile", mobile)
                .append("password", password)
                .append("userFullName", userFullName)
                .append("email", email)
                .append("avatarUrl", avatarUrl)
                .append("wxOpenId", wxOpenId)
                .append("wxWpOpenId", wxWpOpenId)
                .append("wxUnionId", wxUnionId)
                .append("institutionAddress", institutionAddress)
                .append("systemRole", systemRole)
                .append("skipIntro", skipIntro)
                .append("accountType", accountType)
                .append("expired", expired)
                .append("activated", activated)
                .append("expireTime", expireTime)
                .append("isAdmin", isAdmin)
                .toString();
    }
}
