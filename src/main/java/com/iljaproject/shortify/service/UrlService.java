package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;

import java.util.List;

public interface UrlService {

    GenerateShortUrlDto generateShortUrl(String originalUrl);

    List<UrlDto> getAllUrls();

    UrlDto getUrlById(Long id);

    void deleteUrlById(Long id);

    UrlDto getUrlByShortCode(String shortCode);

}
