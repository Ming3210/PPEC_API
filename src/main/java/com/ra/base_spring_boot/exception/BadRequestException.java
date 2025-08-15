package com.ra.base_spring_boot.exception;

public class BadRequestException extends HttpBadRequest {
    public BadRequestException(String message) {
        super(message);
    }
}

