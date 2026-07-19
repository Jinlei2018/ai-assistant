package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.model.chat.ChatMessage;
import com.jinlei.aiassistant.model.chat.Conversation;
import com.jinlei.aiassistant.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationMemoryService {

    private final ConversationRepository repository;

    public ConversationMemoryService(ConversationRepository repository) {
        this.repository = repository;
    }

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

    private Conversation getConversation(String conversationId) {

        return repository.findById(conversationId)
                .orElseGet(() -> {

                    Conversation conversation =
                            new Conversation();

                    repository.save(
                            conversationId,
                            conversation
                    );

                    return conversation;
                });
    }

    public List<ChatMessage> getMessages(String conversationId) {

        return getConversation(conversationId)
                .getMessages();
    }

    public void createConversation(String conversationId) {

        if (!repository.exists(conversationId)) {
            repository.save(conversationId, new Conversation());
        }
    }
}