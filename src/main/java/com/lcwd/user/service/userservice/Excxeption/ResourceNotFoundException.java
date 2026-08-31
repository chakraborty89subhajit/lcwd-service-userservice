package com.lcwd.user.service.userservice.Excxeption;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("resourceNotFoundException");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}