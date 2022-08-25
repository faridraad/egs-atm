package com.egs.bank.configuration.exception;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
