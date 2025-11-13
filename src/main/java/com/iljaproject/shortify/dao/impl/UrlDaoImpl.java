package com.iljaproject.shortify.dao.impl;

import com.iljaproject.shortify.constants.UrlDaoSqlQueries;
import com.iljaproject.shortify.dao.UrlDao;
import com.iljaproject.shortify.dto.CreateUrlDto;
import com.iljaproject.shortify.exception.DuplicateShortUrlException;
import com.iljaproject.shortify.exception.FailedToCreateUrlException;
import com.iljaproject.shortify.exception.FailedToReadFromDatabaseException;
import com.iljaproject.shortify.mapper.UrlRowMapper;
import com.iljaproject.shortify.model.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UrlDaoImpl implements UrlDao {
    private final Logger logger = LoggerFactory.getLogger(UrlDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final UrlRowMapper urlRowMapper;

    public UrlDaoImpl(JdbcTemplate jdbcTemplate, UrlRowMapper urlRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.urlRowMapper = urlRowMapper;
    }

    @Override
    public void createUrl(CreateUrlDto urlDto) {
        try {
            int rows = jdbcTemplate.update(
                    UrlDaoSqlQueries.CREATE_URL_SQL_QUERY,
                    urlDto.originalUrl(),
                    urlDto.shortCode(),
                    LocalDateTime.now()
            );
            logger.info("Inserted {} row(s) into 'urls' table", rows);
        } catch (DuplicateKeyException e) {
            throw new DuplicateShortUrlException("Short code already exists: " + urlDto.shortCode());
        } catch (DataAccessException e) {
            throw new FailedToCreateUrlException("Failed to insert URL into database", e);
        }
    }

    @Override
    public Optional<Url> getUrlById(Long id) {
        try {
            Url fetchedUrl = jdbcTemplate.queryForObject(
              UrlDaoSqlQueries.GET_URL_BY_ID_SQL_QUERY,
              urlRowMapper,
              id
            );
            return Optional.ofNullable(fetchedUrl);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new FailedToReadFromDatabaseException("Failed to read url by id " + id, e);
        }
    }

    @Override
    public List<Url> getUrls() {
        try {
            return jdbcTemplate.query(UrlDaoSqlQueries.GET_URLS_SQL_QUERY, urlRowMapper);
        } catch (DataAccessException e) {
            throw new FailedToReadFromDatabaseException("Failed to read all urls data from database", e);
        }
    }

    @Override
    public Optional<Url> getUrlByOriginalUrl(String originalUrl) {
        try {
            Url fetchedUrl = jdbcTemplate.queryForObject(
                    UrlDaoSqlQueries.GET_URL_BY_ORIGINAL_URL_SQL_QUERY,
                    urlRowMapper,
                    originalUrl
            );
            return Optional.ofNullable(fetchedUrl);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new FailedToReadFromDatabaseException("Failed to read url by original url " + originalUrl, e);
        }
    }
}
