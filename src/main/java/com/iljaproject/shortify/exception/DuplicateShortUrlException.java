package com.iljaproject.shortify.exception;

public class DuplicateShortUrlException extends RuntimeException{
    public DuplicateShortUrlException(String message) {
        super(message);
    }
}
