package com.iljaproject.shortify.dao.impl;

import com.iljaproject.shortify.constants.UrlDaoSqlQueries;
import com.iljaproject.shortify.dao.UrlDao;
import com.iljaproject.shortify.exception.*;
import com.iljaproject.shortify.mapper.UrlRowMapper;
import com.iljaproject.shortify.model.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
    public Long insertOriginalUrl(String originalUrl) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        UrlDaoSqlQueries.CREATE_URL_SQL_QUERY,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, originalUrl);
                ps.setString(2, null);
                ps.setObject(3, LocalDateTime.now());
                return ps;
            }, keyHolder);
            logger.info("Original url {} inserted in database", originalUrl);
            return keyHolder.getKey().longValue();
        } catch (DataAccessException e) {
            throw new FailedToCreateUrlException("Failed to insert URL into database", e);
        }
    }

    @Override
    public void setShortCode(String shortCode, Long id) {
        try {
            jdbcTemplate.update(
                    UrlDaoSqlQueries.UPDATE_URL_WITH_SHORT_CODE_SQL_QUERY,
                    shortCode,
                    id
            );
            logger.info("Short code {} inserted in database", shortCode);
        } catch (DuplicateKeyException e) {
            throw new DuplicateShortUrlException("Short code " + shortCode + " already exists in a database");
        } catch (DataAccessException e) {
            throw new FailedToCreateUrlException("Failed to insert short code into database", e);
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

    @Override
    public void deleteById(Long id) {
        try {
            int rowsAffected = jdbcTemplate.update(
                UrlDaoSqlQueries.DELETE_URL_BY_ID_SQL_QUERY,
                id
            );
            if (rowsAffected == 0) {
                throw new UrlNotFoundException("Url with id " + id + " was not found");
            }
            logger.info("Url object with id {} was removed", id);
        } catch (DataAccessException e) {
            throw new FailedToDeleteUrlException("Failed to delete url with id " + id, e);
        }
    }
}
