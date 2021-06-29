package com.whotw.uaa.security.oauth2.exception;

import com.whotw.common.exception.WhotwException;
import org.springframework.http.HttpStatus;

/**
 * @author EdisonXu
 * @date 2019-07-29
 */

public class OAuth2AccountStatusException extends CustomOAuth2Exception implements WhotwException {
    public OAuth2AccountStatusException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AccountStatusException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        // The spec says this can be unauthorized
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getOAuth2ErrorCode() {
        // Not in the spec
        return "account_status_error";
    }
}
