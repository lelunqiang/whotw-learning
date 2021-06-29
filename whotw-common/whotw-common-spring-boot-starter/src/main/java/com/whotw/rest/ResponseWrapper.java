package com.whotw.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一返回格式为Json
 *
 * @author EdisonXu
 * @date 2019/7/9
 */

public class ResponseWrapper<T> implements Serializable {

    public static final int DEFAULT_CODE = HttpStatus.OK.value();

    private static final long serialVersionUID = 6035200191198571938L;

    private boolean success;
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    private int code = DEFAULT_CODE;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;

    public ResponseWrapper() {
    }

    public ResponseWrapper(boolean success, String msg, T data, int code) {
        this(success, msg, data, code, null);
    }

    public ResponseWrapper(boolean success, String msg, T data, int code, String exception) {
        this.success = success;
        this.data = data;
        this.msg = msg;
        this.code = code;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static <T> ResponseWrapper<T> okResponse(T data){
        return new ResponseWrapper(true, null, data, DEFAULT_CODE);
    }

    public static <T> ResponseWrapper<T>  errorResponse(String errorMsg){
        return errorResponse(errorMsg, DEFAULT_CODE);
    }

    public static <T> ResponseWrapper<T>  errorResponse(Throwable exception){
        return errorResponse(exception, DEFAULT_CODE);
    }

    public static <T> ResponseWrapper<T>  errorResponse(Throwable exception, int code){
        return new ResponseWrapper(false, exception.getMessage(), null, code, exception.getClass().getCanonicalName());
    }

    public static <T> ResponseWrapper<T>  errorResponse(String errorMsg, int code){
        return new ResponseWrapper(false, errorMsg, null, code);
    }

    public static <T> ResponseWrapper<T>  errorResponse(Throwable exception, String errorMsg, int code){
        return new ResponseWrapper(false, errorMsg, null, code, exception.getClass().getCanonicalName());
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "success=" + success +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", exception=" + exception +
                '}';
    }
}
