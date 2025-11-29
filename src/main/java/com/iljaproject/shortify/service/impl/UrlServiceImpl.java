package com.iljaproject.shortify.service.impl;

import com.iljaproject.shortify.dao.UrlDao;
import com.iljaproject.shortify.dto.GenerateShortUrlDto;
import com.iljaproject.shortify.dto.UrlDto;
import com.iljaproject.shortify.mapper.UrlMapper;
import com.iljaproject.shortify.model.Url;
import com.iljaproject.shortify.service.UrlService;
import com.iljaproject.shortify.util.Base62Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    private String rootUrl;
    private final UrlDao urlDao;
    private final UrlMapper urlMapper;
    private final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);

    public UrlServiceImpl(UrlDao urlDao,
                          @Value("${spring.url.root:http://localhost:8080/}") String rootUrl,
                          UrlMapper urlMapper) {
        this.urlDao = urlDao;
        this.rootUrl = rootUrl;
        this.urlMapper = urlMapper;
    }

    @Override
    @Transactional
    public GenerateShortUrlDto generateShortUrl(String originalUrl) {
        Optional<Url> alreadyExistsUrl = urlDao.getUrlByOriginalUrl(originalUrl);
        if (alreadyExistsUrl.isPresent()) {
            String shortUrl = rootUrl + alreadyExistsUrl.get().shortCode();
            logger.info("Original url {} already exists in database with short url {}", originalUrl, shortUrl);
            return new GenerateShortUrlDto(shortUrl, true);
        }
        Long id = urlDao.insertOriginalUrl(originalUrl);
        String shortCode = Base62Encoder.encode(id);
        urlDao.setShortCode(shortCode, id);
        String shortUrl = rootUrl + shortCode;
        logger.info("Short url {} was successfully created for original url {}", shortUrl, originalUrl);
        return new GenerateShortUrlDto(shortUrl, false);
    }

    @Override
    public List<UrlDto> getAllUrls() {
        List<Url> allUrls = urlDao.getUrls();
        return urlListToUrlDtoList(allUrls);
    }

    private List<UrlDto> urlListToUrlDtoList(List<Url> urlList) {
        return urlList.stream().map(urlMapper::toDto).toList();
    }
}
