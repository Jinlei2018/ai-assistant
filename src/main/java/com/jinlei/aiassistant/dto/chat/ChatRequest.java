package com.jinlei.aiassistant.dto.chat;

import lombok.Data;

@Data
public class ChatRequest {

    private String conversationId;

    private String message;

    public ChatRequest() {
    }
}