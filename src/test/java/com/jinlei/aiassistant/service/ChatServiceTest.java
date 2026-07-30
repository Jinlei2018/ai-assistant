package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.provider.AIClient;
import com.jinlei.aiassistant.provider.AIClientFactory;
import com.jinlei.aiassistant.repository.ConversationRepository;
import com.jinlei.aiassistant.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ChatServiceTest {

    private ConversationService conversationService;

    private ChatService chatService;

    private AtomicReference<List<Message>> sentMessages;


    @BeforeEach
    void setup() {

        ConversationRepository repository =
                new InMemoryConversationRepository();


        sentMessages =
                new AtomicReference<>();


        AIClient fakeClient =
                messages -> {

                    sentMessages.set(messages);

                    return Flux.just(
                            "Your favorite color is blue."
                    );
                };


        AIClientFactory factory =
                mock(
                        AIClientFactory.class
                );


        when(factory.getClient())
                .thenReturn(fakeClient);


        AIProperties aiProperties =
                new AIProperties();


        aiProperties.getMemory()
                .setMaxMessages(20);


        aiProperties.getMemory()
                .setSummaryInterval(4);


        conversationService =
                new ConversationService(
                        repository,
                        aiProperties,
                        factory
                );


        chatService =
                new ChatService(
                        factory,
                        conversationService,
                        aiProperties
                );
    }


    @Test
    void shouldIncludeConversationSummaryInAIContext() {

        String conversationId =
                "test-conversation";


        conversationService.updateSummary(
                conversationId,
                """
                User facts:
                - Name is Alice
                - Favorite color is blue
                """
        );

        assertEquals(
                """
                User facts:
                - Name is Alice
                - Favorite color is blue
                """,
                conversationService.getSummary(
                        conversationId
                )
        );


        StepVerifier.create(
                        chatService.chat(
                                conversationId,
                                "What is my favorite color?"
                        )
                )
                .expectNext(
                        "Your favorite color is blue."
                )
                .verifyComplete();


        List<Message> messages =
                sentMessages.get();


        assertNotNull(messages);


        assertTrue(
                messages.stream()
                        .anyMatch(message ->
                                message.getRole()
                                        .equals("system")
                                        &&
                                        message.getContent()
                                                .contains(
                                                        "Favorite color is blue"
                                                )
                        )
        );
    }
}