package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.domain.chat.ConversationInfo;
import com.jinlei.aiassistant.service.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(ConversationController.class)
class ConversationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ConversationService conversationService;


    @Test
    void shouldListConversations() {

        when(
                conversationService.getConversations()
        )
                .thenReturn(
                        List.of(
                                new ConversationInfo(
                                        "123",
                                        "Java Learning",
                                        LocalDateTime.now(),
                                        LocalDateTime.now()
                                )
                        )
                );


        webTestClient
                .get()
                .uri("/api/conversations")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id")
                .isEqualTo("123");
    }
}