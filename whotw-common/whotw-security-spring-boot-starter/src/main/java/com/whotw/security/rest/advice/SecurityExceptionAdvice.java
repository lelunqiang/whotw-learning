package com.whotw.security.rest.advice;

import com.whotw.security.exception.AccountNotFoundException;
import com.whotw.rest.ResponseWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 认证异常的全局拦截器
 *
 * @author EdisonXu
 * @date 2019-07-30
 */
@RestControllerAdvice
@ResponseBody
@Order(10)
public class SecurityExceptionAdvice {

    /**
     * OAuth2Exception OAuth2认证异常
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({OAuth2Exception.class})
    public ResponseWrapper handleException(OAuth2Exception e) {
        return ResponseWrapper.errorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * BadCredentialsException OAuth2认证异常
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AuthenticationException.class, AccountNotFoundException.class})
    public ResponseWrapper handleException(AuthenticationException e) {
        return ResponseWrapper.errorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
