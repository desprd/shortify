package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dao.impl.UrlDaoImpl;
import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.mapper.UrlMapper;
import com.iljaproject.shortify.model.Url;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UrlServiceTest {

    private final String rootUrl = "https://short.ly/";

    private final UrlDaoImpl urlDao = mock();

    private final UrlServiceImpl urlService = new UrlServiceImpl(urlDao, rootUrl, new UrlMapper());

    private final String expectedUrl = rootUrl + "1";

    private final String originalUrl = "https://example.com";

    private final UrlMapper urlMapper = new UrlMapper();

    private final Url exampleUrlFirst = new Url(
            465L,
            "https://example.com",
            "exmpl",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );

    private final Url exampleUrlSecond = new Url(
            345L,
            "https://secondexample.com",
            "scnd",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );


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
        // Given / When
        when(urlDao.getUrlByOriginalUrl(originalUrl)).thenReturn(Optional.of(exampleUrlFirst));
        GenerateShortUrlDto generatedShortUrl = urlService.generateShortUrl(originalUrl);

        // Then
        assertEquals(rootUrl + exampleUrlFirst.shortCode(), generatedShortUrl.shortUrl());
    }

    @Test
    void mockedDao_getAllUrls_returnListOfUrlDto() {
        // Given / When
        when(urlDao.getUrls()).thenReturn(List.of(exampleUrlFirst, exampleUrlSecond));
        List<UrlDto> fetchedUrls = urlService.getAllUrls();

        // Then
        assertEquals(2, fetchedUrls.size());
        assertEquals(urlListToUrlDtoList(List.of(exampleUrlFirst, exampleUrlSecond)), fetchedUrls);
    }

    private List<UrlDto> urlListToUrlDtoList(List<Url> urlList) {
        return urlList.stream().map(urlMapper::toDto).toList();
    }
}
