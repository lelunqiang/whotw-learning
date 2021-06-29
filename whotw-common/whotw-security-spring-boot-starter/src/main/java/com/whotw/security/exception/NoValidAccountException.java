package com.whotw.security.exception;

import com.whotw.common.exception.WhotwException;
import org.springframework.security.authentication.AccountStatusException;

/**
 * @author EdisonXu
 * @date 2019-07-29
 */

public class NoValidAccountException extends AccountStatusException implements WhotwException {

    public NoValidAccountException(String msg, Throwable t) {
        super(msg, t);
    }

    public NoValidAccountException(String msg) {
        super(msg);
    }
}
