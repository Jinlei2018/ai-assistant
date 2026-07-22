package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.repository.ConversationRepository;
import com.jinlei.aiassistant.repository.InMemoryConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversationMemoryServiceTest {

    private ConversationMemoryService service;


    @BeforeEach
    void setup() {

        ConversationRepository repository =
                new InMemoryConversationRepository();

        service =
                new ConversationMemoryService(
                        repository
                );
    }


    @Test
    void shouldSaveAndLoadConversationSummary() {

        String conversationId =
                "test-conversation";

        String summary =
                "User name is Alice and favorite color is blue";

        // update summary
        service.updateSummary(
                conversationId,
                summary
        );


        // read summary
        String result =
                service.getSummary(
                        conversationId
                );


        assertEquals(
                summary,
                result
        );
    }
}