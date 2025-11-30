package com.iljaproject.shortify.mapper;

public interface DtoMapper<E, D> {
    D toDto(E entity);
}
