package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.provider.AIClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ConversationSummaryService {

    private final AIClientFactory factory;
    private final ConversationMemoryService memory;

    public ConversationSummaryService(
            AIClientFactory factory,
            ConversationMemoryService memory
    ) {
        this.factory = factory;
        this.memory = memory;
    }

//    public Mono<String> summarize(String conversationId) {
//        // implement next
//    }
}
