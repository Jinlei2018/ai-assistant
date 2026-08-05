package com.jinlei.aiassistant.domain.chat;

import java.time.LocalDateTime;

public class ConversationInfo {

    private final String id;
    private final String title;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ConversationInfo(
            String id,
            String title,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}