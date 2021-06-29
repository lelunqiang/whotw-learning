package com.whotw.security.exception;

import com.whotw.common.exception.WhotwException;
import org.springframework.security.core.AuthenticationException;

/**
 * @author EdisonXu
 * @date 2019-07-16
 */

public class AccountNotFoundException extends AuthenticationException implements WhotwException {

    public AccountNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
