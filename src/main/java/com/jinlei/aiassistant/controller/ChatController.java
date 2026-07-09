package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.service.OllamaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ChatController {

    private final Log log = LogFactory.getLog(ChatController.class);

    private final OllamaService ollamaService;


    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/")
    public Mono<String> rootEndpoint() {

        return ollamaService.chat(
                "Tell me a short funny story"
        );
    }
}
