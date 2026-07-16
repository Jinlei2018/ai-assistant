package com.jinlei.aiassistant.model.chat;

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
