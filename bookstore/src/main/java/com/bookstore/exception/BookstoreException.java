package com.bookstore.exception;

/**
 * 自定义业务异常
 */
public class BookstoreException extends RuntimeException {
    private final Integer code; // 错误码

    public BookstoreException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
