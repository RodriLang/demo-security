package com.example.demosecurity.exceptions;

public class InactiveGameException extends RuntimeException {
    public InactiveGameException(String message) {
        super(message);
    }
}
