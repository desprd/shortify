package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dao.impl.UrlDaoImpl;
import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.model.Url;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UrlServiceTest {

    private final String rootUrl = "https://short.ly/";

    private final UrlDaoImpl urlDao = mock();

    private final UrlServiceImpl urlService = new UrlServiceImpl(urlDao, rootUrl);

    private final String expectedUrl = rootUrl + "1";

    private final String originalUrl = "https://example.com";



    @Test
    void newOriginalUrl_generateShortUrl_returnShortUrl() {
        // Given
        Long id = 1L;

        // When
        when(urlDao.getUrlByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlDao.insertOriginalUrl(originalUrl)).thenReturn(id);
        doNothing().when(urlDao).setShortCode(anyString(), anyLong());
        GenerateShortUrlDto generatedShortUrl = urlService.generateShortUrl(originalUrl);

        // Then
        assertEquals(expectedUrl, generatedShortUrl.shortUrl());
    }

    @Test
    void alreadyPresentOriginalUrl_generateShortUrl_returnAlreadyExistedShortUrl() {
        // Given
        Url expectedUrl = new Url(
                465L,
                "https://example.com",
                "exmpl",
                LocalDateTime.of(2024, 11, 9, 10, 30, 0),
                LocalDateTime.of(2024, 11, 9, 10, 30, 0),
                0L
        );

        // When
        when(urlDao.getUrlByOriginalUrl(originalUrl)).thenReturn(Optional.of(expectedUrl));
        GenerateShortUrlDto generatedShortUrl = urlService.generateShortUrl(originalUrl);

        //Then
        assertEquals(rootUrl + expectedUrl.shortCode(), generatedShortUrl.shortUrl());
    }
}
