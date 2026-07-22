package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationMemoryService {

    private final ConversationRepository conversationRepository;

    public ConversationMemoryService(ConversationRepository repository) {
        this.conversationRepository = repository;
    }

    public void addUserMessage(
            String conversationId,
            String content
    ) {
        Conversation conversation =
                getConversation(conversationId);

        conversation.addMessage(
                new Message("user", content)
        );

        conversation.setSummary(
                "User likes Java"
        );

        conversationRepository.save(
                conversationId,
                conversation
        );
    }

    public void addAssistantMessage(
            String conversationId,
            String content
    ) {

        Conversation conversation =
                getConversation(conversationId);

        conversation.addMessage(
                new Message("assistant", content)
        );

        conversationRepository.save(
                conversationId,
                conversation
        );
    }

    private Conversation getConversation(String conversationId) {

        return conversationRepository.findById(conversationId)
                .orElseGet(() -> {

                    Conversation conversation =
                            new Conversation();

                    conversationRepository.save(
                            conversationId,
                            conversation
                    );

                    return conversation;
                });
    }

    public List<Message> getMessages(String conversationId) {

        return getConversation(conversationId)
                .getMessages();
    }

    public String getSummary(String conversationId) {

        return getConversation(conversationId)
                .getSummary();
    }

    public List<Message> getRecentMessages(String conversationId, int limit) {

        List<Message> messages = getMessages(conversationId);


        int start =
                Math.max(
                        0,
                        messages.size() - limit
                );


        return messages.subList(
                start,
                messages.size()
        );
    }

    public void createConversation(String conversationId) {

        if (!conversationRepository.exists(conversationId)) {
            conversationRepository.save(conversationId, new Conversation());
        }
    }

    public void updateSummary(String conversationId, String summary) {

        Conversation conversation = getConversation(conversationId);

        conversation.setSummary(summary);

        conversationRepository.save(
                conversationId,
                conversation
        );
    }
}