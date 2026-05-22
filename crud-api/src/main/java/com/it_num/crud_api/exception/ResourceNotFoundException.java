package com.it_num.crud_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Object identifier) {
        super(String.format("%s not found with identifier: %s", resource, identifier));
    }
}