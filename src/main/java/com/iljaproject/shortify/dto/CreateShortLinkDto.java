package com.iljaproject.shortify.dto;

import jakarta.annotation.Nullable;

    public record CreateShortLinkDto(String originalUrl, @Nullable String customCode) {}
