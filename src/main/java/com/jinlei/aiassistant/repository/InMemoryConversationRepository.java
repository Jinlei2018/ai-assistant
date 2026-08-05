package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.domain.chat.Conversation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryConversationRepository implements ConversationRepository {

    private final Map<String, Conversation> conversations =
            new ConcurrentHashMap<>();

    @Override
    public Optional<Conversation> findById(String conversationId) {
        return Optional.ofNullable(conversations.get(conversationId));
    }

    @Override
    public void save(String conversationId,
                     Conversation conversation) {
        conversations.put(conversationId, conversation);
    }

    @Override
    public boolean exists(String conversationId) {
        return conversations.containsKey(conversationId);
    }

    @Override
    public void delete(String conversationId) {
        conversations.remove(conversationId);
    }

    @Override
    public List<String> findAllIds() {

        return conversations.keySet()
                .stream()
                .toList();
    }
}