package com.iljaproject.shortify.mapper;

import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.model.Url;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper implements BaseMapper<Url, UrlDto>{
    @Override
    public UrlDto toDto(Url entity) {
        return new UrlDto(
                entity.id(),
                entity.originalUrl(),
                entity.shortCode(),
                entity.createdAt(),
                entity.lastAccessed(),
                entity.clickCount()
        );
    }

    @Override
    public Url toEntity(UrlDto dto) {
        return new Url(
                dto.id(),
                dto.originalUrl(),
                dto.shortCode(),
                dto.createdAt(),
                dto.lastAccessed(),
                dto.clickCount()
        );
    }
}
