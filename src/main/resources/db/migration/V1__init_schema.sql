CREATE TABLE urls(
    id BIGSERIAL PRIMARY KEY,
    original_url VARCHAR(1000) NOT NUll,
    short_code VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    last_accessed TIMESTAMP,
    click_count BIGINT NOT NULL DEFAULT 0
)