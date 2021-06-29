package com.whotw.security.oauth2.provider.token;

import com.whotw.security.core.AccountDetails;

/**
 * 自定义AuthenticationToken的标识
 *
 * @author EdisonXu
 * @date 2019-07-24
 */

public interface CustomAuthenticationToken {

    AccountDetails getAccountDetails();

    void setAccountDetails(AccountDetails accountDetails);

}
