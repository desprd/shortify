package com.iljaproject.shortify.dao.impl;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UrlDaoImpl implements UrlDao {
    // TODO correct logging and exception handling
    private final Logger logger = LoggerFactory.getLogger(UrlDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final UrlRowMapper urlRowMapper;

    public UrlDaoImpl(JdbcTemplate jdbcTemplate, UrlRowMapper urlRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.urlRowMapper = urlRowMapper;
    }

    @Override
    public void createUrl(CreateUrlDto urlDto) {
        String sql = """
                INSERT INTO urls (original_url, short_code, created_at)
                VALUES(?, ?, ?)
                """;
        try {
            int rows = jdbcTemplate.update(
                    sql,
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
    public Url getUrlById(Long id) {
        return null;
    }

    @Override
    public List<Url> getUrls() {
        String sql = "SELECT * FROM urls";
        try {
            return jdbcTemplate.query(sql, urlRowMapper);
        } catch (DataAccessException e) {
            throw new FailedToReadFromDatabaseException("Failed to read all urls data from database", e);
        }
    }
}
