package com.iljaproject.shortify.controller.it;

import com.iljaproject.shortify.dto.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(
        scripts = {"/cleanup-data-test.sql", "/data-test.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class ApiControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String createMessage = "Original link was shortened successfully";
    private final String okMessage = "Original link already exists in a database";

    private final UrlDto exampleUrlDtoFirst = new UrlDto(
            1L,
            "https://pl.wikipedia.org/wiki/Java",
            "http://localhost:8080/exmpl",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );

    private final UrlDto exampleUrlDtoSecond = new UrlDto(
            2L,
            "https://en.wikipedia.org/wiki/Computer",
            "http://localhost:8080/" + null,
            LocalDateTime.of(2025, 12, 25, 10, 30, 0),
            LocalDateTime.of(2025, 12, 25, 10, 30, 0),
            0L
    );

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shortUrlDtoWithNewUrl_createShortLink_return201AndResponseDtoWithShortUrl() {
        // Given
        CreateShortLinkDto request = new CreateShortLinkDto("http://something", null);
        ShortUrlDto expectedResponse = new ShortUrlDto("http://localhost:8080/3");

        // When
        ResponseEntity<ResponseDto<ShortUrlDto>> response =
                restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/shorten",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        new ParameterizedTypeReference<ResponseDto<ShortUrlDto>>() {}
                );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals(createMessage, response.getBody().message());
        assertEquals(expectedResponse, response.getBody().data());
    }

    @Test
    void shortUrlDtoWithNewUrlAndCustomShortCode_createShortLink_return201AndResponseDtoWithShortUrl() {
        // Given
        CreateShortLinkDto request = new CreateShortLinkDto("http://something", "smth");
        ShortUrlDto expectedResponse = new ShortUrlDto("http://localhost:8080/smth");

        // When
        ResponseEntity<ResponseDto<ShortUrlDto>> response =
                restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/shorten",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        new ParameterizedTypeReference<ResponseDto<ShortUrlDto>>() {}
                );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals(createMessage, response.getBody().message());
        assertEquals(expectedResponse, response.getBody().data());
    }

    @Test
    void shortUrlDtoWithExistingUrl_createShortLink_return200AndResponseDtoWithShortUrl() {
        // Given
        CreateShortLinkDto request = new CreateShortLinkDto("https://pl.wikipedia.org/wiki/Java", null);
        ShortUrlDto expectedResponse = new ShortUrlDto("http://localhost:8080/exmpl");

        // When
        ResponseEntity<ResponseDto<ShortUrlDto>> response =
                restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/shorten",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        new ParameterizedTypeReference<ResponseDto<ShortUrlDto>>() {}
                );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals(okMessage, response.getBody().message());
        assertEquals(expectedResponse, response.getBody().data());
    }

    @Test
    void urlsInDatabase_getAllUrls_return200AndResponseDtoWithListOfUrlDto() {
        // Given / When
        ResponseEntity<ResponseDto<List<UrlDto>>> response =
                restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/get",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ResponseDto<List<UrlDto>>>() {}
                );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals("All urls fetched successfully", response.getBody().message());
        assertEquals(List.of(exampleUrlDtoFirst, exampleUrlDtoSecond), response.getBody().data());
    }

    @Test
    void existingUrlId_getUrlById_return200AndResponseDtoWithUrlDto() {
        // Given
        long id = 1;

        // When
        ResponseEntity<ResponseDto<UrlDto>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/get/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseDto<UrlDto>>() {},
                id
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals("Url fetched by id successfully", response.getBody().message());
        assertEquals(exampleUrlDtoFirst, response.getBody().data());
    }

    @Test
    void nonExistingUrlId_getUrlById_return404AndResponseDtoWithErrorDto() {
        // Given
        long id = 999;

        // When
        ResponseEntity<ResponseDto<ErrorDto>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/get/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseDto<ErrorDto>>() {},
                id
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().success());
        assertEquals(404, response.getBody().statusCode());
        assertEquals("Url object with id 999 was not found", response.getBody().message());
        assertEquals("uri=/api/v1/get/999", response.getBody().data().description());
        assertEquals("UrlNotFoundException", response.getBody().data().exceptionType());
    }

    @Test
    void existingUrlId_deleteUrlById_return204AndNoContent() {
        // Given
        long id = 1;

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                        "http://localhost:" + port + "/api/v1/delete/{id}",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<Void>() {},
                        id
                );

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void nonExistingUrlId_deleteUrlById_return404AndResponseDtoWithErrorDto() {
        // Given
        long id = 999;

        // When
        ResponseEntity<ResponseDto<ErrorDto>> errorResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/delete/{id}",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ResponseDto<ErrorDto>>() {},
                id
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
        assertFalse(errorResponse.getBody().success());
        assertEquals(404, errorResponse.getBody().statusCode());
        assertEquals("Url with id 999 was not found", errorResponse.getBody().message());
        assertEquals("uri=/api/v1/delete/999", errorResponse.getBody().data().description());
        assertEquals("UrlNotFoundException", errorResponse.getBody().data().exceptionType());
    }

}
