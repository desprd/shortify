package com.iljaproject.shortify.dao;

import com.iljaproject.shortify.model.Url;

import java.util.List;
import java.util.Optional;

public interface UrlDao {

    Long insertOriginalUrl(String originalUrl);

    Long insertOriginalUrl(String originalUrl, String shortCode);

    void setShortCode(String shortCode, Long id);

    Optional<Url> getUrlById(Long id);

    List<Url> getUrls();

    Optional<Url> getUrlByOriginalUrl(String originalUrl);

    void deleteById(Long id);

    Optional<Url> getUrlByShortCode(String shortCode);

}
