package com.jinlei.aiassistant.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationMemoryService memory;

    public ConversationService(
            ConversationMemoryService memory
    ) {
        this.memory = memory;
    }

    public String createConversation() {

        String id = UUID.randomUUID().toString();

        memory.createConversation(id);

        return id;
    }

}