package com.user.exception;

import lombok.Getter;

@Getter
public class UserAccessDeniedException extends RuntimeException {

    private String errorCode;

    public UserAccessDeniedException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
