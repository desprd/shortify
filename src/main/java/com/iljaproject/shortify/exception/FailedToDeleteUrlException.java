package com.iljaproject.shortify.exception;

public class FailedToDeleteUrlException extends RuntimeException {

    public FailedToDeleteUrlException(String message) {
        super(message);
    }

    public FailedToDeleteUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
