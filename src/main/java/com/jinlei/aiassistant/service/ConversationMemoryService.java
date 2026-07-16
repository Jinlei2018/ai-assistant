package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.model.chat.ChatMessage;
import com.jinlei.aiassistant.model.chat.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationMemoryService {

    private final Map<String, Conversation> conversations =
            new ConcurrentHashMap<>();

    public void addUserMessage(
            String conversationId,
            String content
    ) {
        Conversation conversation =
                getConversation(conversationId);

        conversation.getMessages().add(
                new ChatMessage("user", content)
        );
    }

    public void addAssistantMessage(String conversationId, String content) {

        Conversation conversation =
                getConversation(conversationId);

        conversation.getMessages().add(
                new ChatMessage("assistant", content)
        );
    }

    private Conversation getConversation(String id) {

        return conversations.computeIfAbsent(
                id,
                key -> new Conversation()
        );
    }

    public List<ChatMessage> getMessages(String conversationId) {

        return getConversation(conversationId)
                .getMessages();
    }

    public void createConversation(String id) {

        conversations.putIfAbsent(
                id,
                new Conversation()
        );
    }
}