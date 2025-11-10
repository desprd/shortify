package com.iljaproject.shortify.dao;

import com.iljaproject.shortify.dto.CreateUrlDto;
import com.iljaproject.shortify.model.Url;

import java.util.List;

public interface UrlDao {

    void createUrl(CreateUrlDto urlDto);

    Url getUrlById(Long id);

    List<Url> getUrls();
}
