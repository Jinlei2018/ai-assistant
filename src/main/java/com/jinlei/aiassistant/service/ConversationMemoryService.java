package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

        conversation.addMessage(
                new Message("user", content)
        );

        conversation.setSummary(
                "User likes Java"
        );

        repository.save(
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

        repository.save(
                conversationId,
                conversation
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

        if (!repository.exists(conversationId)) {
            repository.save(conversationId, new Conversation());
        }
    }

    public void updateSummary(
            String conversationId,
            String summary
    ) {

        Conversation conversation =
                getConversation(conversationId);

        conversation.setSummary(summary);

        repository.save(
                conversationId,
                conversation
        );
    }
}