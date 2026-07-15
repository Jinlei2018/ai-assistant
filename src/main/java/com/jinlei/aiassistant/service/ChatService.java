package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.provider.AIClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final AIClient aiClient;

    public ChatService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public Flux<String> chat(String prompt) {
        return aiClient.chat(prompt);
    }
}