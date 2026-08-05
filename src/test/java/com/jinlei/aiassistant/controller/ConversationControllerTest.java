package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.domain.chat.ConversationInfo;
import com.jinlei.aiassistant.service.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Collections;
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
                                        "summary",
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        List.of()

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

    @Test
    void shouldGetConversation() {

        ConversationInfo info =
                new ConversationInfo(
                        "123",
                        "Java",
                        "User likes Java",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()
                );


        when(
                conversationService.getConversationInfo("123")
        )
                .thenReturn(info);


        webTestClient
                .get()
                .uri("/api/conversations/123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("123");
    }
}