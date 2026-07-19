package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.dto.chat.ConversationResponse;
import com.jinlei.aiassistant.service.ConversationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService service;

    public ConversationController(
            ConversationService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ConversationResponse create() {

        return new ConversationResponse(
                service.createConversation()
        );
    }

}