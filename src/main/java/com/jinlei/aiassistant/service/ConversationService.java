package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.provider.AIClientFactory;
import com.jinlei.aiassistant.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AIProperties aiProperties;
    private final AIClientFactory factory;

    public ConversationService(ConversationRepository repository, AIProperties aiProperties, AIClientFactory factory) {
        this.conversationRepository = repository;
        this.aiProperties = aiProperties;
        this.factory = factory;
    }

    public void addUserMessage(
            String conversationId,
            String content
    ) {
        Conversation conversation = getConversation(conversationId);

        conversation.addMessage(new Message("user", content));

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

    public String createConversation() {

        String conversationId =
                UUID.randomUUID().toString();

        createConversation(conversationId);

        return conversationId;
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

        conversationRepository.save(conversationId, conversation);
    }

    public List<Message> buildChatContext(
            String conversationId,
            int maxMessages
    ) {

        List<Message> context = new ArrayList<>();

        String systemPrompt = aiProperties.getSystemPrompt();

        if (systemPrompt != null && !systemPrompt.isBlank()) {

            context.add(
                    new Message(
                            "system",
                            systemPrompt
                    )
            );
        }

        String summary = getSummary(conversationId);

        if (summary != null && !summary.isBlank()) {

            context.add(
                    new Message(
                            "system",
                            """
                            Conversation summary:
    
                            %s
                            """.formatted(summary)
                    )
            );
        }

        context.addAll(
                getRecentMessages(
                        conversationId,
                        maxMessages
                )
        );

        return context;
    }

    public Mono<Void> maybeUpdateSummary(
            String conversationId
    ) {

        if (!shouldUpdateSummary(conversationId)) {
            return Mono.empty();
        }

        return updateConversationSummary(conversationId);
    }

    private boolean shouldUpdateSummary(String conversationId) {

        int interval = aiProperties.getMemory().getSummaryInterval();

        int messageCount = getMessages(conversationId).size();
        System.out.println(
                "messages="
                        + messageCount
                        + ", interval="
                        + interval
        );

        return messageCount >= interval && messageCount % interval == 0;
    }


    private Mono<Void> updateConversationSummary(
            String conversationId
    ) {

        StringBuilder updatedSummary =
                new StringBuilder();

        return factory
                .getClient()
                .chat(
                        List.of(
                                new Message(
                                        "user",
                                        buildSummaryPrompt(
                                                conversationId
                                        )
                                )
                        )
                )
                .doOnNext(updatedSummary::append)
                .then(
                        Mono.fromRunnable(() ->
                                updateSummary(
                                        conversationId,
                                        updatedSummary.toString()
                                )
                        )
                );
    }


    String buildSummaryPrompt(String conversationId) {

        String currentSummary = getSummary(conversationId);

        List<Message> recentMessages =
                getRecentMessages(
                        conversationId,
                        aiProperties.getMemory()
                                .getMaxMessages()
                );

        String recentConversation =
                recentMessages.stream()
                        .map(message ->
                                message.getRole()
                                        + ": "
                                        + message.getContent()
                        )
                        .collect(Collectors.joining("\n"));


        return """
            Update the following conversation summary.

            Current summary:
            %s

            Recent messages:
            %s

            Instructions:
            - Keep important user facts.
            - Add new information from recent messages.
            - Remove outdated information if necessary.
            - Keep the summary concise.
            - Return only the updated summary.
            """
                .formatted(
                        currentSummary == null
                                ? ""
                                : currentSummary,
                        recentConversation
                );
    }
}