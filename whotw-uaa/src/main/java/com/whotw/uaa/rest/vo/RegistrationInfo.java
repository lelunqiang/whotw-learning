package com.whotw.uaa.rest.vo;

import com.whotw.uaa.rest.dto.AccountDTO;

/**
 * 注册信息
 *
 * @author EdisonXu
 * @date 2020-02-12
 */
public interface RegistrationInfo {
    String getSignature();

    Long getInstitutionId();

    AccountDTO toAccountDTO();

    void setMobile(String mobile);

    String getMobile();
}
