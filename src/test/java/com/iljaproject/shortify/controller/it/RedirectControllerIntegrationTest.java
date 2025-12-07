package com.iljaproject.shortify.controller.it;

import com.iljaproject.shortify.dto.ErrorDto;
import com.iljaproject.shortify.dto.ResponseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(
        scripts = {"/cleanup-data-test.sql", "/data-test.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class RedirectControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        });
    }

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
    void existingShortCode_redirect_return302AndRedirectToOriginalLink() {
        // Given
        String shortCode = "exmpl";
        URI expectedUri = URI.create("https://pl.wikipedia.org/wiki/Java");

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/{shortCode}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Void>() {},
                shortCode
        );
        
        // Then
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(response.getHeaders().getLocation(), expectedUri);
    }

    @Test
    void nonExistingShortCode_redirect_return404AndResponseDtoWithErrorDto() {
        // Given
        String shortCode = "asd";
        String expectedErrorMessage = "Url object with short code asd was not found";

        // When
        ResponseEntity<ResponseDto<ErrorDto>> response = restTemplate.exchange(
                "http://localhost:" + port + "/{shortCode}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseDto<ErrorDto>>() {},
                shortCode
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().success());
        assertEquals(404, response.getBody().statusCode());
        assertEquals(expectedErrorMessage, response.getBody().message());
        assertEquals("uri=/asd", response.getBody().data().description());
        assertEquals("UrlNotFoundException", response.getBody().data().exceptionType());
    }

}
