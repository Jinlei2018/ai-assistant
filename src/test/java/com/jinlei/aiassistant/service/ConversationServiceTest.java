package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.config.AIProperties;
import com.jinlei.aiassistant.domain.chat.Conversation;
import com.jinlei.aiassistant.domain.chat.ConversationInfo;
import com.jinlei.aiassistant.entity.chat.ConversationEntity;
import com.jinlei.aiassistant.mapper.ConversationMapper;
import com.jinlei.aiassistant.provider.AIClient;
import com.jinlei.aiassistant.provider.AIClientFactory;
import com.jinlei.aiassistant.repository.ConversationRepository;
import com.jinlei.aiassistant.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class ConversationServiceTest {

    private ConversationService conversationService;
    private AIProperties aiProperties;
    private AIClientFactory factory;
    private ConversationMapper mapper;


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

        mapper = new ConversationMapper();
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

    @Test
    void shouldSetCreatedAndUpdatedAtWhenConversationIsCreated() {

        String conversationId =
                conversationService.createConversation();

        assertNotNull(
                conversationService.getCreatedAt(
                        conversationId
                )
        );

        assertNotNull(
                conversationService.getUpdatedAt(
                        conversationId
                )
        );

        assertEquals(
                conversationService.getCreatedAt(
                        conversationId
                ),
                conversationService.getUpdatedAt(
                        conversationId
                )
        );
    }

    @Test
    void shouldListConversationsSortedByUpdatedTime()
            throws InterruptedException {

        String first =
                conversationService.createConversation();

        conversationService.updateTitle(
                first,
                "First Conversation"
        );

        Thread.sleep(10);

        String second =
                conversationService.createConversation();

        conversationService.updateTitle(
                second,
                "Second Conversation"
        );

        List<ConversationInfo> conversations =
                conversationService.getConversations();

        assertEquals(
                2,
                conversations.size()
        );

        ConversationInfo newest =
                conversations.get(0);

        assertEquals(
                second,
                newest.getId()
        );

        assertEquals(
                "Second Conversation",
                newest.getTitle()
        );

        assertNotNull(
                newest.getCreatedAt()
        );

        assertNotNull(
                newest.getUpdatedAt()
        );

        assertNotNull(
                newest.getMessages()
        );

        ConversationInfo oldest =
                conversations.get(1);

        assertEquals(
                first,
                oldest.getId()
        );

        assertEquals(
                "First Conversation",
                oldest.getTitle()
        );
    }

    @Test
    void shouldDeleteConversation() {

        String id = conversationService.createConversation();

        conversationService.deleteConversation(id);

        assertEquals(
                0,
                conversationService.getConversations().size()
        );
    }

    @Test
    void shouldUpdateConversationEntity() {

        Conversation conversation = new Conversation();

        conversation.setTitle("Java");
        conversation.setSummary("User likes Java");

        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10);
        LocalDateTime updatedAt = LocalDateTime.now();

        conversation.setCreatedAt(createdAt);
        conversation.setUpdatedAt(updatedAt);

        ConversationEntity entity =
                new ConversationEntity("123");

        mapper.updateEntity(
                conversation,
                entity
        );

        assertEquals(
                "Java",
                entity.getTitle()
        );

        assertEquals(
                "User likes Java",
                entity.getSummary()
        );

        assertEquals(
                createdAt,
                entity.getCreatedAt()
        );

        assertEquals(
                updatedAt,
                entity.getUpdatedAt()
        );
    }
}