package com.jinlei.aiassistant.controller;

import com.jinlei.aiassistant.model.chat.ChatRequest;
import com.jinlei.aiassistant.service.OllamaService;
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

    private final OllamaService ollamaService;


    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    // it returns text like "data:Dependency
    //data: Injection
    //data: (
    //data:DI
    //data:)
    //data: is
    //data: a
    //data: design" , Normally the frontend (JavaScript, React, Vue, etc.) receives: "data:Hello data: World" and strips off the data: prefix before displaying
//    @GetMapping(
//            value="/",
//            produces = MediaType.TEXT_EVENT_STREAM_VALUE
//    )
//    public Flux<String> rootEndpoint() {
//        return ollamaService.chatStream(
//                "Explain dependency injection in Spring Boot."
//        );
//    }

    @PostMapping(
            value = "/api/chat",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> chat(
            @RequestBody ChatRequest request
    ) {
        return ollamaService.chatStream(
                request.getMessage()
        );
    }
}
