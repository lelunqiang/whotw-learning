package com.whotw.security.exception;

import com.whotw.common.exception.WhotwException;
import org.springframework.security.core.AuthenticationException;

/**
 * @author EdisonXu
 * @date 2019-07-24
 */

public class ValidationCodeAuthenticationException extends AuthenticationException implements WhotwException {
    public ValidationCodeAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidationCodeAuthenticationException(String msg) {
        super(msg);
    }
}
