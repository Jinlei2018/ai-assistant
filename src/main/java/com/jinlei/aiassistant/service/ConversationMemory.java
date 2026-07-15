package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.model.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationMemory {

    private final List<ChatMessage> messages =
            new ArrayList<>();


    public void addUserMessage(String content) {

        messages.add(
                new ChatMessage(
                        "user",
                        content
                )
        );
    }


    public void addAssistantMessage(String content) {

        messages.add(
                new ChatMessage(
                        "assistant",
                        content
                )
        );
    }


    public List<ChatMessage> getMessages() {

        return messages;
    }
}