package com.ra.base_spring_boot.exception;


public class ConflictException extends HttpConflict {
    public ConflictException(String message) {
        super(message);
    }
}

