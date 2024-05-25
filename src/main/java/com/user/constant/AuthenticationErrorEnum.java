package com.user.constant;

import lombok.Getter;

@Getter
public enum AuthenticationErrorEnum {

    BAD_CREDENTIALS("AUTH001", "The username or password is incorrect"),
    ACCESS_DENIED("AUTH002", "Access to this resource is not authorized"),
    SIGNATURE_EXCEPTION("AUTH003", "The JWT signature is invalid"),
    EXPIRED_JWT("AUTH004", "The JWT token has expired"),
    UNKNOWN_ERROR("AUTH005", "Unknown internal server error");

    private String errorCode;
    private String errorMessage;

    private AuthenticationErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
