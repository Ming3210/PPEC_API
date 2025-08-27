package com.ra.base_spring_boot.advice;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

