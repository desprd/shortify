package com.iljaproject.shortify.service;

import com.iljaproject.shortify.dao.impl.UrlDaoImpl;
import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.exception.UrlNotFoundException;
import com.iljaproject.shortify.mapper.UrlMapper;
import com.iljaproject.shortify.model.Url;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class UrlServiceTest {

    private final String rootUrl = "https://short.ly/";

    private final UrlDaoImpl urlDao = mock();

    private final UrlServiceImpl urlService = new UrlServiceImpl(urlDao, rootUrl, new UrlMapper(rootUrl));

    private final String expectedUrl = rootUrl + "1";

    private final String originalUrl = "https://example.com";

    private final UrlMapper urlMapper = new UrlMapper(rootUrl);

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

    @Test
    void existingUrlId_getUrlById_returnUrlDto() {
        // Given / When
        when(urlDao.getUrlById(465L)).thenReturn(Optional.of(exampleUrlFirst));
        UrlDto fetchedDto = urlService.getUrlById(465L);

        //Then
        assertEquals(urlMapper.toDto(exampleUrlFirst), fetchedDto);
    }

    @Test
    void nonExistingUrlId_getUrlById_throwUrlNotFoundException() {
        // Given / When
        when(urlDao.getUrlById(999L)).thenReturn(Optional.empty());
        Throwable e = assertThrows(
                UrlNotFoundException.class,
                () -> urlService.getUrlById(999L)
        );

        // Then
        assertEquals("Url object with id 999 was not found", e.getMessage());
    }

    @Test
    void existingUrlShortCode_getUrlByShortCode_returnUrlDto() {
        // Given
        String shortCode = "exmpl";
        UrlDto expectedDto = urlMapper.toDto(exampleUrlFirst);

        // When
        when(urlDao.getUrlByShortCode(shortCode)).thenReturn(Optional.of(exampleUrlFirst));
        UrlDto fetchedDto = urlService.getUrlByShortCode(shortCode);

        // Then
        assertEquals(expectedDto, fetchedDto);
    }

    @Test
    void nonExistingUrlShortCode_getUrlByShortCode_throwUrlNotFoundException() {
        // Given
        String shortCode = "asd";
        String expectedMessage = "Url object with short code asd was not found";

        // When
        when(urlDao.getUrlByShortCode(shortCode)).thenReturn(Optional.empty());
        Throwable e = assertThrows(
                UrlNotFoundException.class,
                () -> urlService.getUrlByShortCode(shortCode)
        );

        // Then
        assertEquals(expectedMessage, e.getMessage());
    }

    private List<UrlDto> urlListToUrlDtoList(List<Url> urlList) {
        return urlList.stream().map(urlMapper::toDto).toList();
    }
}
