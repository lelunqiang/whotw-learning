package com.whotw.uaa.rest.vo;

import com.whotw.common.data.*;
import com.whotw.security.domain.Account;
import com.whotw.uaa.entity.AccountRoleEntity;
import com.whotw.uaa.rest.dto.AccountDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-12-13
 */
public class RegistrationVO implements Serializable, RegistrationInfo {

    private static final long serialVersionUID = -3734559251642000413L;

    //个人部分信息
    protected String name;
    protected Long userId;
    protected String avatarUrl;
    protected String systemRole;
    protected List<ApplicationRole> applicationRoles;
    protected String signature;
    protected String mobile;

    //机构部分信息
    protected String institutionName;
    protected String institutionType;
    protected Long institutionId;

    //地理位置信息
    protected String latitude;
    protected String longitude;
    protected String province;
    protected String city;
    protected String district;
    //机构地址+详细地址
    protected String address;
    //机构地址
    protected String institutionAddress;
    //详细地址
    protected String detailAddress;

    protected String invitationCode;

    protected Account.AccountType accountType = Account.AccountType.TRIAL;

    public String getInstitutionAddress() {
        return institutionAddress;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ApplicationRole> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(List<ApplicationRole> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    @Override
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public Account.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(Account.AccountType accountType) {
        this.accountType = accountType;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    @Override
    public AccountDTO toAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setMobile(this.mobile);
        accountDTO.setInstitutionId(this.institutionId);
        accountDTO.setAccountType(this.accountType);
        accountDTO.setUserId(this.getUserId());
        accountDTO.setInstitutionName(this.getInstitutionName());
        accountDTO.setUserFullName(this.getName());
        accountDTO.setAvatarUrl(this.getAvatarUrl());
        accountDTO.setInstitutionAddress(this.getAddress());
        accountDTO.setCreatedTime(LocalDateTime.now(ZoneId.systemDefault()));
        SystemRole role = SystemRole.from(this.getSystemRole());
        if (role == null) // should never happen
            throw new IllegalArgumentException("非法的身份");
        accountDTO.setSystemRole(role);
        if (CollectionUtils.isNotEmpty(applicationRoles)) {
            accountDTO.setApplicationRoles(
                    applicationRoles
                            .stream()
                            .map(r -> new AccountRoleEntity(r.getId(), r.getDescription(), institutionId, institutionName))
                            .collect(Collectors.toList()));
        }
        return accountDTO;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("userId", userId)
                .append("avatarUrl", avatarUrl)
                .append("systemRole", systemRole)
                .append("applicationRoles", applicationRoles)
                .append("signature", signature)
                .append("mobile", mobile)
                .append("institutionName", institutionName)
                .append("institutionType", institutionType)
                .append("institutionId", institutionId)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("province", province)
                .append("city", city)
                .append("district", district)
                .append("address", address)
                .append("invitationCode", invitationCode)
                .append("accountType", accountType)
                .toString();
    }
}
