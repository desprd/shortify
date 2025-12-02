package com.iljaproject.shortify.controller.mock;

import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.exception.DuplicateShortUrlException;
import com.iljaproject.shortify.exception.UrlNotFoundException;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerMockTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlServiceImpl urlService;

    private final GenerateShortUrlDto shortUrl = new GenerateShortUrlDto("shrt.com/ex", false);

    private final UrlDto exampleUrlDtoFirst = new UrlDto(
            465L,
            "https://example.com",
            "exmpl",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );

    private final UrlDto exampleUrlDtoSecond = new UrlDto(
            345L,
            "https://secondexample.com",
            "scnd",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );

    @Test
    void originalUrl_mockCreateShortLink_return201AndResponseDtoWithShortUrl() throws Exception {
        when(urlService.generateShortUrl(anyString())).thenReturn(shortUrl);
        mockMvc.perform(post("/api/v1/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "originalUrl": "https://example.com" }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.shortUrl").value(shortUrl.shortUrl()));
    }

    @Test
    void originalUrl_mockCreateShortLinkToThrowDuplicateShortUrlException_return500AndTrowsDuplicateShortUrlException()
            throws Exception {
        when(urlService.generateShortUrl(anyString())).thenThrow(DuplicateShortUrlException.class);
        mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                { "originalUrl": "https://example.com" }
                """))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void mockedDatabase_mockGetAllUrls_return200AndResponseDtoWithListOfUrlDto() throws Exception {
        when(urlService.getAllUrls()).thenReturn(List.of(exampleUrlDtoFirst, exampleUrlDtoSecond));
        mockMvc.perform(get("/api/v1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(exampleUrlDtoFirst.id()))
                .andExpect(jsonPath("$.data[0].originalUrl").value(exampleUrlDtoFirst.originalUrl()))
                .andExpect(jsonPath("$.data[1].id").value(exampleUrlDtoSecond.id()));
    }

    @Test
    void existingUrlId_mockGetUrlById_return200AndResponseDtoWithUrlDto() throws Exception {
        when(urlService.getUrlById(465L)).thenReturn(exampleUrlDtoFirst);
        mockMvc.perform(get("/api/v1/get/465"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.id").value(exampleUrlDtoFirst.id()))
                .andExpect(jsonPath("$.data.originalUrl").value(exampleUrlDtoFirst.originalUrl()));
    }

    @Test
    void nonExistingUrlId_mockGetUrlById_return404AndErrorResponseDto() throws Exception {
        when(urlService.getUrlById(anyLong())).thenThrow(UrlNotFoundException.class);
        mockMvc.perform(get("/api/v1/get/465"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void existingUrlId_mockDeleteUrlById_return204AndNoContent() throws Exception{
        doNothing().when(urlService).deleteUrlById(465L);
        mockMvc.perform(delete("/api/v1/delete/465"))
                .andExpect(status().isNoContent());
    }

    @Test
    void nonExistingUrlId_mockDeleteUrlById_return404AndErrorResponseDto() throws Exception {
        doThrow(UrlNotFoundException.class).when(urlService).deleteUrlById(465L);
        mockMvc.perform(delete("/api/v1/delete/465"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

}


