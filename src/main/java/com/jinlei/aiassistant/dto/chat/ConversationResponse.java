package com.jinlei.aiassistant.dto.chat;

import lombok.Data;

@Data
public class ConversationResponse {

    private String conversationId;

    public ConversationResponse() {
    }

    public ConversationResponse(String conversationId) {
        this.conversationId = conversationId;
    }


}
