package com.jinlei.aiassistant.domain.chat;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConversationInfo {

    private final String id;
    private final String title;
    private final String summary;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<Message> messages;

    public ConversationInfo(
            String id,
            String title, String summary,
            LocalDateTime createdAt,
            LocalDateTime updatedAt, List<Message> messages
    ) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
    }

}