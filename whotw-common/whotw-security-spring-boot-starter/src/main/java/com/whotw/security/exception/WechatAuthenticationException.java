package com.whotw.security.exception;

import com.whotw.common.exception.WhotwException;
import org.springframework.security.core.AuthenticationException;

/**
 * 微信认证失败的异常
 *
 * @author EdisonXu
 * @date 2019/7/12
 */

public class WechatAuthenticationException extends AuthenticationException implements WhotwException {

    public WechatAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public WechatAuthenticationException(String msg) {
        super(msg);
    }
}
