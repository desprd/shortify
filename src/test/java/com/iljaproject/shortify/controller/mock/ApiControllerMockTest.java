package com.iljaproject.shortify.controller.mock;

import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.exception.DuplicateShortUrlException;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    void createShortLinkDto_mockCreateShortLink_return201AndResponseDtoWithShortUrl() throws Exception {
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
    void createShortLinkDto_mockCreateShortLinkToThrowDuplicateShortUrlException_return500AndTrowsDuplicateShortUrlException()
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
}
