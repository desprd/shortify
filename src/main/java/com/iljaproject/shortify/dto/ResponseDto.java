package com.iljaproject.shortify.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ResponseDto<T> (
        boolean success,
        int statusCode,
        String message,
        T data
) {
    public static <T> ResponseEntity<ResponseDto<T>> ok(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(true, 200, message, data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(HttpStatus statusCode, String message, T data) {
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDto<>(false, statusCode.value(), message, data));
    }

    public static <T> ResponseEntity<ResponseDto<T>> created(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto<>(true, 201, message, data));
    }

}
