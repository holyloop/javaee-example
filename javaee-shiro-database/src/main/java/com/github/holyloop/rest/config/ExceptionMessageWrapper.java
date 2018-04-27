package com.github.holyloop.rest.config;

/**
 * 对外异常消息的封装
 * 
 * @author holyloop
 */
public class ExceptionMessageWrapper {

    private int code;
    private String message;

    public ExceptionMessageWrapper(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
