package com.iljaproject.shortify.controller.it;

import com.iljaproject.shortify.dto.CreateShortLinkDto;
import com.iljaproject.shortify.dto.ResponseDto;
import com.iljaproject.shortify.dto.ShortUrlDto;
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
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    @Test
    void createShortLinkDtoWithNewUrl_createShortLink_return201AndResponseDtoWithShortUrl() {
        // Given
        CreateShortLinkDto request = new CreateShortLinkDto("http://something");
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
        assertEquals(expectedResponse, response.getBody().data()); // now works!
    }

    @Test
    void createShortLinkDtoWithExistingUrl_createShortLink_return200AndResponseDtoWithShortUrl() {
        // Given
        CreateShortLinkDto request = new CreateShortLinkDto("https://example.com");
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
}
