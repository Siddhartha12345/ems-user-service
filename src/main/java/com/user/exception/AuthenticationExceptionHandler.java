package com.user.exception;

import com.user.constant.AuthenticationErrorEnum;
import com.user.response.JwtErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JwtErrorResponse> handleSecurityException(Exception exception) {
        JwtErrorResponse response = null;
        HttpStatus status = null;
        if(exception instanceof BadCredentialsException) {
            response = JwtErrorResponse.builder()
                    .errorCode(AuthenticationErrorEnum.BAD_CREDENTIALS.getErrorCode())
                    .errorMessage(AuthenticationErrorEnum.BAD_CREDENTIALS.getErrorMessage())
                    .build();
            status = HttpStatus.UNAUTHORIZED;
        } if (exception instanceof UserAccessDeniedException) {
            response = JwtErrorResponse.builder()
                    .errorCode(((UserAccessDeniedException) exception).getErrorCode())
                    .errorMessage(exception.getMessage())
                    .build();
            status = HttpStatus.FORBIDDEN;
        } if (exception instanceof SignatureException) {
            response = JwtErrorResponse.builder()
                    .errorCode(AuthenticationErrorEnum.SIGNATURE_EXCEPTION.getErrorCode())
                    .errorMessage(AuthenticationErrorEnum.SIGNATURE_EXCEPTION.getErrorMessage())
                    .build();
            status = HttpStatus.FORBIDDEN;
        } if (exception instanceof ExpiredJwtException) {
            response = JwtErrorResponse.builder()
                    .errorCode(AuthenticationErrorEnum.EXPIRED_JWT.getErrorCode())
                    .errorMessage(AuthenticationErrorEnum.EXPIRED_JWT.getErrorMessage())
                    .build();
            status = HttpStatus.FORBIDDEN;
        } if(response == null) {
            response = JwtErrorResponse.builder()
                    .errorCode(AuthenticationErrorEnum.UNKNOWN_ERROR.getErrorCode())
                    .errorMessage(AuthenticationErrorEnum.UNKNOWN_ERROR.getErrorMessage())
                    .build();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }
}