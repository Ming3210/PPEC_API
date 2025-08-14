package com.ra.base_spring_boot.advice;

public class PartnerAlreadyExistsException extends RuntimeException {
    public PartnerAlreadyExistsException(String message) {
        super(message);
    }
}
