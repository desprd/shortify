package com.iljaproject.shortify.controller.mock;

import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.exception.UrlNotFoundException;
import com.iljaproject.shortify.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class RedirectControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlServiceImpl urlService;

    private final UrlDto exampleUrlDtoFirst = new UrlDto(
            465L,
            "https://example.com",
            "exmpl",
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            LocalDateTime.of(2024, 11, 9, 10, 30, 0),
            0L
    );

    @Test
    void existingShortCode_mockRedirect_return302AndRedirectToOriginalLink() throws Exception {
        when(urlService.getUrlByShortCode("exmpl")).thenReturn(exampleUrlDtoFirst);
        mockMvc.perform(get("/exmpl"))
                .andExpect(status().isFound())
                .andExpect(header().string("location", "https://example.com"));
    }

    @Test
    void existingShortCode_mockRedirect_return404AndErrorResponseDto() throws Exception {
        when(urlService.getUrlByShortCode("exmpl")).thenThrow(UrlNotFoundException.class);
        mockMvc.perform(get("/exmpl"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

}
