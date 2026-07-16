package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.provider.AIClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final AIClientFactory factory;
    private final ConversationMemory memory;

    public ChatService(
            AIClientFactory factory,
            ConversationMemory memory
    ) {
        this.factory = factory;
        this.memory = memory;
    }

    public Flux<String> chat(String prompt) {

        memory.addUserMessage(prompt);

        return factory
                .getClient()
                .chat(memory.getMessages())
                .doOnNext(response ->
                        memory.addAssistantMessage(response)
                );
    }
}