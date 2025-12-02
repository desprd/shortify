package com.iljaproject.shortify.controller;

import com.iljaproject.shortify.dto.*;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get")
    public ResponseEntity<ResponseDto<List<UrlDto>>> getAllUrls() {
        List<UrlDto> allUrls = urlService.getAllUrls();
        if (allUrls.isEmpty()) {
            return ResponseDto.ok(
                    "No urls in database",
                    allUrls
            );
        }
        return ResponseDto.ok(
                "All urls fetched successfully",
                allUrls
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDto<UrlDto>> getUrlById(@PathVariable long id) {
        UrlDto requiredUrl = urlService.getUrlById(id);
        return ResponseDto.ok(
                "Url fetched by id successfully",
                requiredUrl
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUrlById(@PathVariable long id) {
        urlService.deleteUrlById(id);
        return ResponseEntity.noContent().build();
    }
}
