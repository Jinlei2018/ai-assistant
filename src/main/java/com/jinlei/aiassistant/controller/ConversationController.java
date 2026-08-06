package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.domain.chat.ConversationInfo;
import com.jinlei.aiassistant.dto.chat.ConversationResponse;
import com.jinlei.aiassistant.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<ConversationInfo> getConversations() {

        return service.getConversations();
    }

    @GetMapping({"/{conversationId}"})
    public ConversationInfo getConversation(
            @PathVariable String conversationId
    ) {

        return service.getConversationInfo(
                conversationId
        );
    }

    @DeleteMapping("/{conversationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConversation(
            @PathVariable String conversationId
    ) {
        service.deleteConversation(conversationId);
    }

}