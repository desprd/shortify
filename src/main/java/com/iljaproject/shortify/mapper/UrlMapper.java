package com.iljaproject.shortify.mapper;

import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.model.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper implements DtoMapper<Url, UrlDto>{

    private String rootUrl;

    public UrlMapper(@Value("${spring.url.root:http://localhost:8080/}") String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Override
    public UrlDto toDto(Url entity) {
        return new UrlDto(
                entity.id(),
                entity.originalUrl(),
                rootUrl + entity.shortCode(),
                entity.createdAt(),
                entity.lastAccessed(),
                entity.clickCount()
        );
    }
}