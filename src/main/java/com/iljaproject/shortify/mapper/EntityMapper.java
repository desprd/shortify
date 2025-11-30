package com.iljaproject.shortify.mapper;

public interface EntityMapper<E, D> {
    E toEntity(D dto);
}
