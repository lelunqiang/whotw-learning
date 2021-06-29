package com.whotw.uaa.security.oauth2.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author EdisonXu
 * @date 2019-07-29
 */

public class CustomOAuth2Exception extends OAuth2Exception {

    public CustomOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomOAuth2Exception(String msg) {
        super(msg);
    }
}
