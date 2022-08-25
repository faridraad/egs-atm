package com.egs.atm.configuration.exception;

public class FeignCustomException extends RuntimeException {
    private final Integer  status;
    private final String body;

    public FeignCustomException(Integer status, String message) {
        this.body = message;
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getBody() {
        return this.body;
    }

    public static class ServiceUnavailable extends RuntimeException {
        private final String message;

        public ServiceUnavailable(String message) {
            super(message);
            this.message = message;
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }
}
