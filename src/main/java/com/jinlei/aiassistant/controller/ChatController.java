package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.service.OllamaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ChatController {

    private final Log log = LogFactory.getLog(ChatController.class);

    private final OllamaService ollamaService;


    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping(
            value="/",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> rootEndpoint() {
        return ollamaService.chatStream(
                "Explain dependency injection in Spring Boot."
        );
    }
}
