package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;
import jakarta.annotation.Nullable;

import java.util.List;

public interface UrlService {

    GenerateShortUrlDto generateShortUrl(String originalUrl, @Nullable String customCode);

    List<UrlDto> getAllUrls();

    UrlDto getUrlById(Long id);

    void deleteUrlById(Long id);

    UrlDto getUrlByShortCode(String shortCode);

}
