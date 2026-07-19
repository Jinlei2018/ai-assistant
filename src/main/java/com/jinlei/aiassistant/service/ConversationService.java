package com.jinlei.aiassistant.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationMemoryService conversationMemoryService;

    public ConversationService(ConversationMemoryService memory) {
        this.conversationMemoryService = memory;
    }

    public String createConversation() {

        String conversationId = UUID.randomUUID().toString();

        conversationMemoryService.createConversation(conversationId);

        return conversationId;
    }

}