package com.iljaproject.shortify.dto;

import java.time.LocalDateTime;

public record UrlDto(Long id,
                     String originalUrl,
                     String shortCode,
                     LocalDateTime createdAt,
                     LocalDateTime lastAccessed,
                     Long clickCount) {}
