package com.jinlei.aiassistant.repository;

import com.jinlei.aiassistant.model.chat.Conversation;

import java.util.Optional;

public interface ConversationRepository {

    Optional<Conversation> findById(String conversationId);

    void save(String conversationId, Conversation conversation);

    boolean exists(String conversationId);

    void delete(String conversationId);

}