package com.whotw.security.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.whotw.common.data.ApplicationRole;
import com.whotw.common.data.SystemRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Objects;

/**
 * 系统用户的账号信息
 *
 * @author EdisonXu
 * @date 2019-07-16
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account implements Serializable {

    private static final long serialVersionUID = -6301962007749903512L;

    protected Long id;

    protected Long userId;

    protected Long institutionId;

    protected String institutionName;

    protected String institutionLogo;

    protected String username;

    protected String mobile;

    protected String password;

    protected String userFullName;

    protected String relation;

    protected String email;

    protected String avatarUrl;

    protected String wxOpenId;

    protected String wxUnionId;

    protected String wxSessionKey;

    protected String province;

    protected String city;

    protected String district;

    protected String qrCode;

    protected SystemRole systemRole;

    protected ApplicationRole applicationRole;

    protected Boolean skipIntro = false;

    protected AccountType accountType = AccountType.TRIAL;

    protected boolean expired;

    protected boolean locked;

    protected boolean credentialsExpired;

    protected boolean activated = true;

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

    public String getWxSessionKey() {
        return wxSessionKey;
    }

    public void setWxSessionKey(String wxSessionKey) {
        this.wxSessionKey = wxSessionKey;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getInstitutionLogo() {
        return institutionLogo;
    }

    public Account setInstitutionLogo(String institutionLogo) {
        this.institutionLogo = institutionLogo;
        return this;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public String getRelation() {
        return relation;
    }

    public Account setRelation(String relation) {
        this.relation = relation;
        return this;
    }

    public String getQrCode() {
        return qrCode;
    }

    public Account setQrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return expired == account.expired &&
                locked == account.locked &&
                credentialsExpired == account.credentialsExpired &&
                activated == account.activated &&
                Objects.equals(id, account.id) &&
                Objects.equals(userId, account.userId) &&
                Objects.equals(institutionId, account.institutionId) &&
                Objects.equals(institutionName, account.institutionName) &&
                Objects.equals(username, account.username) &&
                Objects.equals(mobile, account.mobile) &&
                Objects.equals(password, account.password) &&
                Objects.equals(userFullName, account.userFullName) &&
                Objects.equals(relation, account.relation) &&
                Objects.equals(email, account.email) &&
                Objects.equals(avatarUrl, account.avatarUrl) &&
                Objects.equals(wxOpenId, account.wxOpenId) &&
                Objects.equals(wxUnionId, account.wxUnionId) &&
                Objects.equals(wxSessionKey, account.wxSessionKey) &&
                Objects.equals(province, account.province) &&
                Objects.equals(city, account.city) &&
                Objects.equals(district, account.district) &&
                systemRole == account.systemRole &&
                Objects.equals(applicationRole, account.applicationRole) &&
                Objects.equals(skipIntro, account.skipIntro) &&
                accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, institutionId, institutionName, username, mobile, password, userFullName, relation, email, avatarUrl, wxOpenId, wxUnionId, wxSessionKey, province, city, district, systemRole, applicationRole, skipIntro, accountType, expired, locked, credentialsExpired, activated);
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
                .append("relation", relation)
                .append("email", email)
                .append("avatarUrl", avatarUrl)
                .append("wxOpenId", wxOpenId)
                .append("wxUnionId", wxUnionId)
                .append("wxSessionKey", wxSessionKey)
                .append("province", province)
                .append("city", city)
                .append("district", district)
                .append("qrCode", qrCode)
                .append("systemRole", systemRole)
                .append("applicationRole", applicationRole)
                .append("skipIntro", skipIntro)
                .append("accountType", accountType)
                .append("expired", expired)
                .append("locked", locked)
                .append("credentialsExpired", credentialsExpired)
                .append("activated", activated)
                .toString();
    }

    /**
     * 账户类型
     */
    public static enum AccountType {
        /**
         * 试用
         */
        TRIAL("试用"),
        /**
         * 正式
         */
        FORMAL("正式");

        private String description;

        AccountType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static AccountType fromDescription(String description){
            if(StringUtils.isEmpty(description))
                return null;
            for(AccountType each: AccountType.values()){
                if(each.description.equals(description))
                    return each;
            }
            return null;
        }

        public static AccountType from(String value){
            if(StringUtils.isEmpty(value))
                return null;
            for(AccountType each: AccountType.values()){
                if(each.name().equals(value))
                    return each;
            }
            return null;
        }
    }

}
