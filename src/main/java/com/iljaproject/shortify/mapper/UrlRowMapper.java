package com.iljaproject.shortify.mapper;

import com.iljaproject.shortify.model.Url;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class UrlRowMapper implements RowMapper<Url> {
    @Override
    public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
        Url url = new Url();
        url.setId(rs.getLong("id"));
        url.setOriginalUrl(rs.getString("original_url"));
        url.setShortCode(rs.getString("short_code"));
        url.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        url.setLastAccessed(rs.getObject("last_accessed", LocalDateTime.class));
        url.setClickCount(rs.getLong("click_count"));
        return url;
    }
}
