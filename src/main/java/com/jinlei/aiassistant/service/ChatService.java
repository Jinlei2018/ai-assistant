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

    private static final Logger log =
            LoggerFactory.getLogger(ChatService.class);

    private final AIClientFactory factory;
    private final ConversationService conversationService;
    private final AIProperties aiProperties;

    public ChatService(
            AIClientFactory factory,
            ConversationService conversationService,
            AIProperties aiProperties
    ) {
        this.factory = factory;
        this.conversationService = conversationService;
        this.aiProperties = aiProperties;
    }

    public Flux<String> chat(
            String conversationId,
            String prompt
    ) {

        conversationService.addUserMessage( // add new prompt to messages
                conversationId,
                prompt
        );

        List<Message> messages =
                conversationService.buildChatContext(
                        conversationId,
                        aiProperties.getMemory().getMaxMessages()
                );

        log.info("Sending {} messages:", messages.size());

        messages.forEach(message ->
                log.info("{}: {}",
                        message.getRole(),
                        message.getContent())
        );

        StringBuilder assistantResponse =
                new StringBuilder();

        return factory
                .getClient()
                .chat(messages)
                .doOnNext(assistantResponse::append)
                .doOnComplete(() -> {

                    conversationService.addAssistantMessage(
                            conversationId,
                            assistantResponse.toString()
                    );

                    conversationService
                            .maybeUpdateTitle(conversationId)
                            .subscribe();

                    conversationService
                            .maybeUpdateSummary(conversationId)
                            .subscribe();
                });
    }
}