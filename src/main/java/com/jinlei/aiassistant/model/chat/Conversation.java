package com.jinlei.aiassistant.model.chat;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private final List<ChatMessage> messages =
            new ArrayList<>();

    public List<ChatMessage> getMessages() {
        return messages;
    }

}