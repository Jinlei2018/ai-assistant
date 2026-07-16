package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.provider.AIClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final AIClientFactory factory;
    private final ConversationMemoryService memory;

    public ChatService(
            AIClientFactory factory,
            ConversationMemoryService memory
    ) {
        this.factory = factory;
        this.memory = memory;
    }

    public Flux<String> chat(
            String conversationId,
            String prompt
    ) {
        memory.addUserMessage(
                conversationId,
                prompt
        );

        return factory
                .getClient()
                .chat(
                        memory.getMessages(conversationId)
                )
                .doOnNext(response ->
                        memory.addAssistantMessage(
                                conversationId,
                                response
                        )
                );
    }
}