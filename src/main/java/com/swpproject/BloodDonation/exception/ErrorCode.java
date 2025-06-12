package com.swpproject.BloodDonation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1001, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(1002, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "Unauthorized", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD(1005, "Invalid password", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_INVALID(1006, "New password is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1007, "Passwords do not match", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;
}