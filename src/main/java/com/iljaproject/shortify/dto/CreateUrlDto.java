package com.iljaproject.shortify.dto;

public record CreateUrlDto(
        String originalUrl,
        String shortCode) {}
