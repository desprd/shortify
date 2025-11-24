package com.iljaproject.shortify.dto;

import java.time.LocalDateTime;

public record ErrorDto(
        String description,
        String exceptionType,
        LocalDateTime timestamp
) {}
