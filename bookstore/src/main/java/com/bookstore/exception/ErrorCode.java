package com.bookstore.exception;

/**
 * 错误代码枚举
 */
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    INVALID_CREDENTIALS(1002, "Invalid username or password"),
    BOOK_NOT_FOUND(2001, "Book not found"),
    INSUFFICIENT_STOCK(2002, "Insufficient stock"),
    ORDER_CREATION_FAILED(3001, "Order creation failed");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
