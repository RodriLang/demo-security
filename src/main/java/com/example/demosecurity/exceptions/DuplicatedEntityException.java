package com.example.demosecurity.exceptions;

public class DuplicatedEntityException extends RuntimeException {
    public DuplicatedEntityException(String message) {
        super(message);
    }
}
