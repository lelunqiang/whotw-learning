package com.whotw.common.exception;

/**
 * 业务异常，其消息会被正确返回到前端
 *
 * @author EdisonXu
 * @date 2019/7/9
 */

public class BusinessException extends RuntimeException implements WhotwException{
    private static final long serialVersionUID = -805682779490412058L;

    public BusinessException(String message) {
        super(message);
    }
}
