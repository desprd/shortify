package com.iljaproject.shortify.exception;

public class FailedToCreateUrlException extends RuntimeException{
    public FailedToCreateUrlException(String message) {
        super(message);
    }

    public FailedToCreateUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
