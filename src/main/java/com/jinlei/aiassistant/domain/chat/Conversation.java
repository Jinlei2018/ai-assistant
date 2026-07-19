package com.jinlei.aiassistant.domain.chat;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {

        messages.add(message);
    }


    public List<Message> getMessages() {

        return messages;

    }

}