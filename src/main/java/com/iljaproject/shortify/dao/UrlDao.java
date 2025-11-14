package com.iljaproject.shortify.dao;

import com.iljaproject.shortify.model.Url;

import java.util.List;
import java.util.Optional;

public interface UrlDao {

    Long insertOriginalUrl(String originalUrl);

    Optional<Url> getUrlById(Long id);

    List<Url> getUrls();

    Optional<Url> getUrlByOriginalUrl(String originalUrl);
}
