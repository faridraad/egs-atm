package com.egs.bank.configuration.exception;

public class NumberOfAttemptsExceededException extends RuntimeException {
    public NumberOfAttemptsExceededException(String message) {
        super(message);
    }
}
