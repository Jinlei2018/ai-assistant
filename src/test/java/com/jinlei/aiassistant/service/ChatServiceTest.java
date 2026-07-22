package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.provider.AIClient;
import com.jinlei.aiassistant.provider.AIClientFactory;
import com.jinlei.aiassistant.repository.ConversationRepository;
import com.jinlei.aiassistant.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


class ChatServiceTest {

    private ConversationMemoryService memory;

    private ChatService chatService;

    private AtomicReference<List<Message>> sentMessages;


    @BeforeEach
    void setup() {

        ConversationRepository repository = new InMemoryConversationRepository();

        memory = new ConversationMemoryService(repository);

        sentMessages = new AtomicReference<>();

        AIClient fakeClient =
                messages -> {

                    sentMessages.set(messages);

                    return Flux.just(
                            "Your favorite color is blue."
                    );
                };

        AIClientFactory factory = Mockito.mock(AIClientFactory.class);

        when(factory.getClient())
                .thenReturn(fakeClient);

        AIProperties properties = new AIProperties();

        properties.getMemory().setMaxMessages(20);

        chatService = new ChatService(factory, memory, properties);
    }


    @Test
    void shouldIncludeConversationSummaryInAIContext() {

        String conversationId =
                "test-conversation";

        memory.updateSummary(
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
                memory.getSummary(conversationId)
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

        System.out.println("Messages sent to AI:");

        sentMessages.get()
                .forEach(message ->
                        System.out.println(
                                message.getRole()
                                        + ": "
                                        + message.getContent()
                        )
                );

        List<Message> messages =
                sentMessages.get();

        assertTrue(
                messages.stream()
                        .anyMatch(message ->
                                message.getRole()
                                        .equals("system")
                                        &&
                                        message.getContent()
                                                .contains(
                                                        "User likes Java"
                                                )
                        )
        );
    }
}