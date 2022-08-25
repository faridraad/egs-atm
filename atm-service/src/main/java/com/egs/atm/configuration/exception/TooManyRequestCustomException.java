package com.egs.atm.configuration.exception;

public class TooManyRequestCustomException extends RuntimeException {
    public TooManyRequestCustomException(String message) {
        super(message);
    }
}
