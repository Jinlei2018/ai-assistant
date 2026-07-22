package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.provider.AIClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final AIClientFactory factory;
    private final ConversationMemoryService memory;
    private final AIProperties aiProperties;
    private static final Logger log =
            LoggerFactory.getLogger(ChatService.class);

    public ChatService(
            AIClientFactory factory,
            ConversationMemoryService memory,
            AIProperties aiProperties
    ) {
        this.factory = factory;
        this.memory = memory;
        this.aiProperties = aiProperties;
    }

    public Flux<String> chat(
            String conversationId,
            String prompt
    ) {
        memory.addUserMessage(
                conversationId,
                prompt
        );

        List<Message> messages = new ArrayList<>();

        String summary = memory.getSummary(conversationId);

        if (summary != null && !summary.isBlank()) {

            messages.add(
                    new Message(
                            "system",
                            """
                            Conversation summary:
        
                            %s
                            """.formatted(summary)
                    )
            );
        }

        messages.addAll(
                memory.getRecentMessages(
                        conversationId,
                        aiProperties.getMemory()
                                .getMaxMessages()
                )
        );

        log.info("Sending {} messages:", messages.size());

        messages.forEach(message ->
                log.info("{}: {}", message.getRole(), message.getContent())
        );


        StringBuilder assistantResponse = new StringBuilder();

        return factory
                .getClient()
                .chat(messages)
                .doOnNext(assistantResponse::append)
                .doOnComplete(() ->
                        memory.addAssistantMessage(
                                conversationId,
                                assistantResponse.toString()
                        )
                );
    }
}