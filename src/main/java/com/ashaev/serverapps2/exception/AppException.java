package com.ashaev.serverapps2.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, Object... args) {
        super(errorCode.getFormattedMessage(args));
        this.errorCode = errorCode;
    }
}