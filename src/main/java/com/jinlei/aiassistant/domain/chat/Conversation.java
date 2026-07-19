package com.jinlei.aiassistant.domain.chat;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private final List<Message> messages =
            new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

}