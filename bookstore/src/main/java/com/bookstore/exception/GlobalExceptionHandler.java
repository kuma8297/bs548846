package com.bookstore.exception;

import com.bookstore.common.Result;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获 401 Unauthorized 错误
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(401, "Unauthorized: " + e.getMessage());
    }

    /**
     * 捕获类型转换异常
     */
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleTypeMismatchException(TypeMismatchException e) {
        String message = "Invalid value for type '" + e.getRequiredType().getSimpleName() + "': " + e.getValue();
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 捕获 NumberFormatException
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleNumberFormatException(NumberFormatException e) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), "Invalid number format: " + e.getMessage());
    }

    /**
     * 捕获其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        return Result.error(500, "Internal Server Error: " + e.getMessage());
    }
}
