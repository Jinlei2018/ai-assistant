package com.jinlei.aiassistant.entity.chat;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    private String id;

    private Instant createdAt;

    public ConversationEntity() {
    }

    public ConversationEntity(String id) {
        this.id = id;
        this.createdAt = Instant.now();
    }


    public String getId() {
        return id;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }
}