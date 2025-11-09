package com.iljaproject.shortify.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Url {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessed;
    private Long clickCount;

    public Url(){};

    public Url(
            Long id,
            String originalUrl,
            String shortCode,
            LocalDateTime createdAt,
            LocalDateTime lastAccessed,
            Long clickCount
    ) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.lastAccessed = lastAccessed;
        this.clickCount = clickCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(id, url.id) && Objects.equals(originalUrl, url.originalUrl) && Objects.equals(shortCode, url.shortCode) && Objects.equals(createdAt, url.createdAt) && Objects.equals(lastAccessed, url.lastAccessed) && Objects.equals(clickCount, url.clickCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originalUrl, shortCode, createdAt, lastAccessed, clickCount);
    }
}
