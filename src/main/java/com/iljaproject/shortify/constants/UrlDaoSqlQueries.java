package com.iljaproject.shortify.constants;

public final class UrlDaoSqlQueries {
    private UrlDaoSqlQueries() {};
    public static final String GET_URLS_SQL_QUERY = "SELECT * FROM urls";
    public static final String CREATE_URL_SQL_QUERY = """
                INSERT INTO urls (original_url, short_code, created_at)
                VALUES(?, ?, ?)
                """;
    public static final String GET_URL_BY_ID_SQL_QUERY = """
            SELECT * FROM urls
            WHERE id = ?
            LIMIT 1;
            """;
    public static final String GET_URL_BY_ORIGINAL_URL_SQL_QUERY = """
            SELECT * FROM urls
            WHERE original_url = ?
            LIMIT 1;
            """;
    public static final String UPDATE_URL_WITH_SHORT_CODE_SQL_QUERY = """
            UPDATE urls
            SET short_code = ?
            WHERE id = ?
            """;
}