package com.iljaproject.shortify.constants;

public class UrlDaoSqlQueries {
    private UrlDaoSqlQueries() {};
    public static final String GET_URLS_SQL_QUERY = "SELECT * FROM urls";
    public static final String CREATE_URL_SQL_QUERY = """
                INSERT INTO urls (original_url, short_code, created_at)
                VALUES(?, ?, ?)
                """;

}
