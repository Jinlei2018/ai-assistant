package com.jinlei.aiassistant.model.chat;

import lombok.Data;

@Data
public class ChatRequest {

    private String conversationId;

    private String message;

    public ChatRequest() {
    }
}