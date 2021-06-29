package com.whotw.gateway.exception;

/**
 * @author EdisonXu
 * @date 2019-08-09
 */
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -3028456157471545696L;

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
