package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.model.chat.ChatRequest;
import com.jinlei.aiassistant.service.ChatService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final Log log = LogFactory.getLog(ChatController.class);

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(
            value="/api/chat",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> chat(
            @RequestBody ChatRequest request
    ) {
        return chatService.chat(request.getMessage());
    }
}
