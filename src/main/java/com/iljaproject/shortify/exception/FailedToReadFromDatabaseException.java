package com.iljaproject.shortify.exception;

public class FailedToReadFromDatabaseException extends RuntimeException{
    public FailedToReadFromDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
