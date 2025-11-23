package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dto.GenerateShortUrlDto;

public interface UrlService {
    GenerateShortUrlDto generateShortUrl(String originalUrl);
}
