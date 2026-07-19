package com.jinlei.aiassistant.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Getter
    private String role;


    @Getter
    @Column(columnDefinition = "TEXT")
    private String content;


    private Instant createdAt;


    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private ConversationEntity conversation;


    protected MessageEntity() {
    }


    public MessageEntity(
            String role,
            String content
    ) {
        this.role = role;
        this.content = content;
        this.createdAt = Instant.now();
    }



}