package io.tinypiggy.demo.exception;

public class ServiceException extends RuntimeException{

    private int code;
    private String msg;

    public ServiceException() {
        this(1001, "服务错误");
    }

    public ServiceException(String msg) {
        this(1001, msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
