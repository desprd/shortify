package com.iljaproject.shortify.controller;

import com.iljaproject.shortify.dto.CreateShortLinkDto;
import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.ResponseDto;
import com.iljaproject.shortify.dto.ShortUrlDto;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final UrlServiceImpl urlService;

    public ApiController(UrlServiceImpl urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ResponseDto<ShortUrlDto>> createShortLink(
            @RequestBody CreateShortLinkDto createShortLinkDto
    ) {
        GenerateShortUrlDto generatedShortUrl = urlService.generateShortUrl(createShortLinkDto.originalUrl());
        if (generatedShortUrl.existedBefore()) {
            return ResponseDto.ok(
                    "Original link already exists in a database",
                    new ShortUrlDto(generatedShortUrl.shortUrl())
            );
        }
        return ResponseDto.created(
                "Original link was shortened successfully",
                new ShortUrlDto(generatedShortUrl.shortUrl())
        );
    }
}
