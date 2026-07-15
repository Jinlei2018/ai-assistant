package com.jinlei.aiassistant.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final OllamaService ollamaService;

    public ChatService(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    public Flux<String> chat(String prompt) {
        return ollamaService.chatStream(prompt);
    }
}