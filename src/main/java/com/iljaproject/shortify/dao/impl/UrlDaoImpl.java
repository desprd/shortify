package com.iljaproject.shortify.dao.impl;

import com.iljaproject.shortify.dao.UrlDao;
import com.iljaproject.shortify.mapper.UrlRowMapper;
import com.iljaproject.shortify.model.Url;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UrlDaoImpl implements UrlDao {

    private final JdbcTemplate jdbcTemplate;
    private final UrlRowMapper urlRowMapper;

    public UrlDaoImpl(JdbcTemplate jdbcTemplate, UrlRowMapper urlRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.urlRowMapper = urlRowMapper;
    }

    @Override
    public void createUrl(Url url) {
        
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
