package com.iljaproject.shortify.controller;

import com.iljaproject.shortify.dto.ResponseDto;
import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
    public class RedirectController {

    private final UrlService service;

    public RedirectController(UrlService service) {
        this.service = service;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        UrlDto fetchedDto = service.getUrlByShortCode(shortCode);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(fetchedDto.originalUrl()))
                .build();
    }

}
