package com.whotw.uaa.rest.vo;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * @author EdisonXu
 * @date 2019-07-17
 */
@ApiModel("新用户注册VO")
public class AccountRegisterVO extends RegistrationVO {

    private static final long serialVersionUID = -4458234130294622279L;

    @NotEmpty(message = "必须填写名字")
    @Override
    public String getName() {
        return super.getName();
    }

    @NotEmpty
    @Override
    public String getSignature() {
        return super.getSignature();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountRegisterVO that = (AccountRegisterVO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(systemRole, that.systemRole) &&
                Objects.equals(signature, that.signature) &&
                Objects.equals(institutionName, that.institutionName) &&
                Objects.equals(institutionType, that.institutionType) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(district, that.district) &&
                Objects.equals(address, that.address) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(applicationRoles, that.applicationRoles) &&
                Objects.equals(institutionId, that.institutionId) &&
                Objects.equals(accountType, that.accountType)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userId, avatarUrl, systemRole, signature, institutionName, institutionType, latitude,
                longitude, province, city, district, address, mobile, applicationRoles, institutionId, accountType);
    }

}
