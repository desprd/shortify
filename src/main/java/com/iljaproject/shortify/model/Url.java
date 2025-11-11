package com.iljaproject.shortify.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record Url( Long id,
        String originalUrl,
        String shortCode,
        LocalDateTime createdAt,
        LocalDateTime lastAccessed,
        Long clickCount) {}
