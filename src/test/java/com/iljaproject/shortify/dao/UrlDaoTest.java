package com.iljaproject.shortify.dao;

import com.iljaproject.shortify.dao.impl.UrlDaoImpl;
import com.iljaproject.shortify.dto.CreateUrlDto;
import com.iljaproject.shortify.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeEach
    void setUp() {
        Url first = new Url(
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
                "smth",
                LocalDateTime.of(2025, 12, 25, 10, 30, 0),
                LocalDateTime.of(2025, 12, 25, 10, 30, 0),
                0L
        );
        expectedUrls = new ArrayList<>(List.of(first, second));
    }

    @Test
    void testDbWithUrls_getUrls_expectedUrls() {
        // Given / When
        List<Url> fetchedUrls = urlDao.getUrls();

        // Then
        assertEquals(expectedUrls, fetchedUrls);
    }

    @Test
    void url_createUrl_testDbWithInsertedUrl() {
        // Given
        CreateUrlDto urlDto = new CreateUrlDto("https://longurl.com", "short");

        // When
        urlDao.createUrl(urlDto);
        List<Url> updatedUrls = urlDao.getUrls();
        assertEquals(3, updatedUrls.size());
    }

}
