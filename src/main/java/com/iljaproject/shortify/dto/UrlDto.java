package com.iljaproject.shortify.dto;

import java.time.LocalDateTime;

public record UrlDto(Long id,
                     String originalUrl,
                     String shortUrl,
                     LocalDateTime createdAt,
                     LocalDateTime lastAccessed,
                     Long clickCount) {}
