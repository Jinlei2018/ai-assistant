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
                messages ->
                        Flux.just(
                                "User likes Java and Spring"
                        );


        AIClientFactory factory = mock(AIClientFactory.class);

        when(factory.getClient()).thenReturn(fakeClient);

        AIProperties properties = new AIProperties();

        properties.getMemory().setSummaryInterval(4);

        conversationService = new ConversationService(repository, properties, factory);
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
}