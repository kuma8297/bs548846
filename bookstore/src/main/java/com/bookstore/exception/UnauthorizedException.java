package com.bookstore.exception;

/**
 * 自定义异常类 - 未授权
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
