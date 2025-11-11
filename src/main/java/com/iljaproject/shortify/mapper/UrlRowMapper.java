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
        return new Url(
                rs.getLong("id"),
                rs.getString("original_url"),
                rs.getString("short_code"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getObject("last_accessed", LocalDateTime.class),
                rs.getLong("click_count")
        );
    }
}
