package com.jinlei.aiassistant.domain.rag;

import java.time.LocalDateTime;
import java.util.UUID;

public class Document {

    private final String id;

    private final String content;

    private final LocalDateTime createdAt;


    public Document(String content) {

        this.id =
                UUID.randomUUID().toString();

        this.content = content;

        this.createdAt =
                LocalDateTime.now();
    }


    public String getId() {
        return id;
    }


    public String getContent() {
        return content;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
