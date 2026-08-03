package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.provider.AIClient;
import com.jinlei.aiassistant.provider.AIClientFactory;
import com.jinlei.aiassistant.repository.ConversationRepository;
import com.jinlei.aiassistant.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class ConversationServiceTest {

    private ConversationService conversationService;
    private AIProperties aiProperties;
    private AIClientFactory factory;


    @BeforeEach
    void setup() {

        ConversationRepository repository =
                new InMemoryConversationRepository();

        AIClient fakeClient =
                messages -> {

                    String prompt =
                            messages.get(0).getContent();

                    if (prompt.contains("Generate a short title")) {
                        return Flux.just("Java Learning");
                    }

                    return Flux.just("User likes Java and Spring");
                };

        factory = mock(AIClientFactory.class);

        when(factory.getClient())
                .thenReturn(fakeClient);


        aiProperties = new AIProperties();

        aiProperties.getMemory()
                .setSummaryInterval(4);

        aiProperties.getMemory()
                .setMaxMessages(20);


        conversationService =
                new ConversationService(
                        repository,
                        aiProperties,
                        factory
                );
    }

    @Test
    void shouldSaveAndLoadConversationSummary() {

        String conversationId =
                "test-conversation";

        String summary =
                "User name is Alice and favorite color is blue";

        // update summary
        conversationService.updateSummary(
                conversationId,
                summary
        );


        // read summary
        String result =
                conversationService.getSummary(
                        conversationId
                );


        assertEquals(
                summary,
                result
        );
    }

    @Test
    void shouldUpdateSummaryWhenMessageCountReachesInterval() {

        String conversationId = "test-conversation";


        conversationService.createConversation(conversationId);


        conversationService.addUserMessage(
                conversationId,
                "I like Java"
        );

        conversationService.addAssistantMessage(
                conversationId,
                "Java is great"
        );

        conversationService.addUserMessage(
                conversationId,
                "I use Spring Boot"
        );

        conversationService.addAssistantMessage(
                conversationId,
                "Spring Boot is popular"
        );

        StepVerifier.create(
                        conversationService
                                .maybeUpdateSummary(
                                        conversationId
                                )
                )
                .verifyComplete();

        assertEquals(
                "User likes Java and Spring",
                conversationService.getSummary(
                        conversationId
                )
        );
    }

    @Test
    void shouldNotUpdateSummaryBeforeInterval() {

        String conversationId = "test-conversation";


        conversationService.createConversation(conversationId);


        conversationService.addUserMessage(
                conversationId,
                "I like Java"
        );

        StepVerifier.create(
                        conversationService.maybeUpdateSummary(
                                conversationId
                        )
                )
                .verifyComplete();


        assertEquals(
                null,
                conversationService.getSummary(
                        conversationId
                )
        );
    }

    @Test
    void shouldUseRecentMessagesWhenUpdatingSummary() {

        String conversationId = "test-summary";


        conversationService.addUserMessage(
                conversationId,
                "My name is Alice"
        );

        conversationService.addAssistantMessage(
                conversationId,
                "Nice to meet you"
        );


        conversationService.addUserMessage(
                conversationId,
                "I like Java"
        );


        String prompt =
                conversationService.buildSummaryPrompt(
                        conversationId
                );


        assertTrue(
                prompt.contains("I like Java")
        );


        assertTrue(
                prompt.contains("Current summary:")
        );
    }

    @Test
    void shouldGenerateConversationTitle() {

        String conversationId = "test-conversation";

        conversationService.createConversation(conversationId);

        conversationService.addUserMessage(
                conversationId,
                "I want to learn Java"
        );

        conversationService.addAssistantMessage(
                conversationId,
                "Java is a programming language."
        );

        conversationService.addUserMessage(
                conversationId,
                "Can you teach me Spring Boot?"
        );

        conversationService.addAssistantMessage(
                conversationId,
                "Sure, let's start with dependency injection."
        );

        StepVerifier.create(
                        conversationService.maybeUpdateTitle(
                                conversationId
                        )
                )
                .verifyComplete();

        assertEquals(
                "Java Learning",
                conversationService.getTitle(
                        conversationId
                )
        );
    }

    @Test
    void shouldNotGenerateTitleTwice() {

        String conversationId = "test-conversation";

        conversationService.createConversation(conversationId);

        conversationService.updateTitle(
                conversationId,
                "Existing Title"
        );

        StepVerifier.create(
                        conversationService.maybeUpdateTitle(
                                conversationId
                        )
                )
                .verifyComplete();

        assertEquals(
                "Existing Title",
                conversationService.getTitle(
                        conversationId
                )
        );
    }
}