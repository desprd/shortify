package com.iljaproject.shortify.dao.impl;

import com.iljaproject.shortify.dao.UrlDao;
import com.iljaproject.shortify.dto.CreateUrlDto;
import com.iljaproject.shortify.mapper.UrlRowMapper;
import com.iljaproject.shortify.model.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        int insert = jdbcTemplate.update(
                sql,
                urlDto.originalUrl(),
                urlDto.shortCode(),
                LocalDateTime.now()
        );
        if (insert == 1) {
            logger.info("Row in 'urls' table created successfully");
        } else {
            // TODO throw custom exception
        }
    }

    @Override
    public Url getUrlById(Long id) {
        return null;
    }

    @Override
    public List<Url> getUrls() {
        String sql = "SELECT * FROM urls";
        return jdbcTemplate.query(sql, urlRowMapper);
    }
}
