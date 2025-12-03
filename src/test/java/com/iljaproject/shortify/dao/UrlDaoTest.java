package com.iljaproject.shortify.dao;

import com.iljaproject.shortify.dao.impl.UrlDaoImpl;
import com.iljaproject.shortify.exception.DuplicateShortUrlException;
import com.iljaproject.shortify.exception.UrlNotFoundException;
import com.iljaproject.shortify.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(
        scripts = {"/cleanup-data-test.sql", "/data-test.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class UrlDaoTest {
    @Autowired
    private UrlDaoImpl urlDao;
    private List<Url> expectedUrls;
    private Url first;
    @Autowired
    private JdbcTemplate template;


    @BeforeEach
    void setUp() {
        first = new Url(
                1L,
                "https://example.com",
                "exmpl",
                LocalDateTime.of(2024, 11, 9, 10, 30, 0),
                LocalDateTime.of(2024, 11, 9, 10, 30, 0),
                0L
        );
        Url second = new Url(
                2L,
                "https://something.com",
                null,
                LocalDateTime.of(2025, 12, 25, 10, 30, 0),
                LocalDateTime.of(2025, 12, 25, 10, 30, 0),
                0L
        );
        expectedUrls = new ArrayList<>(List.of(first, second));
    }

    @Test
    void testDbWithUrls_getUrls_returnExpectedUrls() {
        // Given / When
        List<Url> fetchedUrls = urlDao.getUrls();

        // Then
        assertEquals(expectedUrls, fetchedUrls);
    }

    @Test
    @Sql(scripts = {"/cleanup-data-test.sql"})
    void testDbWithNoData_getUrls_returnEmptyList() {
        // Given / When
        List<Url> fetchedUrls = urlDao.getUrls();

        // Then
        assertTrue(fetchedUrls.isEmpty());
    }

    @Test
    void originalUrl_insertOriginalUrl_insertedUrlId() {
        // Given
        String originalUrl = "https://longurl.com";

        // When
        Long id = urlDao.insertOriginalUrl(originalUrl);
        Url fetchedUrl = urlDao.getUrlById(id).get();

        // Then
        assertEquals(3, id);
        assertEquals(3, fetchedUrl.id());
        assertEquals(originalUrl, fetchedUrl.originalUrl());
        assertNull(fetchedUrl.shortCode());
    }


    @Test
    void correctId_getById_returnExpectedUrl() {
        // Given / When
        Optional<Url> fetchedUrl = urlDao.getUrlById(1L);

        // Then
        assertTrue(fetchedUrl.isPresent());
        assertEquals(first, fetchedUrl.get());

    }

    @Test
    void nonExistingId_getById_returnOptionalEmpty() {
        // Given / When
        Optional<Url> fetchedUrl = urlDao.getUrlById(10L);

        // Then
        assertTrue(fetchedUrl.isEmpty());
    }

    @Test
    void correctOriginalUrl_getUrlByOriginalUrl_returnExpectedUrl() {
        // Given / When
        Optional<Url> fetchedUrl = urlDao.getUrlByOriginalUrl("https://example.com");

        // Then
        assertTrue(fetchedUrl.isPresent());
        assertEquals(first, fetchedUrl.get());
    }

    @Test
    void nonExistingOriginalUrl_getUrlByOriginalUrl_returnOptionalEmpty() {
        // Given / When
        Optional<Url> fetchedUrl = urlDao.getUrlByOriginalUrl("https://nonexisting.com");

        // Then
        assertTrue(fetchedUrl.isEmpty());
    }

    @Test
    void uniqueShortCode_setShortCode_urlWithShortCode() {
        // Given
        String shortCode = "smth";

        // When
        urlDao.setShortCode(shortCode, 2L);
        Url secondUrl = urlDao.getUrlById(2L).get();

        // Then
        assertEquals("smth", secondUrl.shortCode());
    }

    @Test
    void nonUniqueShortCode_setShortCode_throwKeyAlreadyExistsException() {
        // Given
        String shortCode = "exmpl";

        // When
        Throwable e = assertThrows(
                DuplicateShortUrlException.class,
                () -> urlDao.setShortCode(shortCode, 2L)
        );

        // Then
        assertEquals("Short code exmpl already exists in a database", e.getMessage());
    }

    @Test
    void existingId_deleteById_deletedUrlObject() {
        // Given
        Long id = 1L;

        // When
        urlDao.deleteById(id);
        Optional<Url> deleteUrl = urlDao.getUrlById(id);

        // Then
        assertTrue(deleteUrl.isEmpty());
    }

    @Test
    void nonExistingId_deleteById_throwUrlNotFoundException() {
        // Given
        Long id = 999L;

        // When
        Throwable e = assertThrows(
                UrlNotFoundException.class,
                () -> urlDao.deleteById(id)
        );

        // Then
        assertEquals("Url with id 999 was not found", e.getMessage());
    }


    @Test
    void existingShortCode_getUrlByShortCode_returnUrlObject() {
        // Given
        String shortCode = "exmpl";

        // When
        Optional<Url> fetchedUrl = urlDao.getUrlByShortCode(shortCode);

        // Then
        assertTrue(fetchedUrl.isPresent());
        assertEquals(first, fetchedUrl.get());
    }

    @Test
    void nonExistingShortCode_getUrlByShortCode_throwUrlNotFoundException() {
        // Given
        String shortCode = "asdfg";

        // When
        Optional<Url> fetchedUrl = urlDao.getUrlByShortCode(shortCode);

        // Then
        assertTrue(fetchedUrl.isEmpty());
    }

}