package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.provider.AIClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final AIClientFactory factory;

    public ChatService(AIClientFactory factory) {
        this.factory = factory;
    }

    public Flux<String> chat(String prompt) {
        return factory
                .getClient()
                .chat(prompt);
    }
}