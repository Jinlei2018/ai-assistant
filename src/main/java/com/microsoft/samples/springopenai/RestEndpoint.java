package com.microsoft.samples.springopenai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RestEndpoint {

    private final Log log = LogFactory.getLog(RestEndpoint.class);

    private final OllamaService ollamaService;


    public RestEndpoint(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/")
    public Mono<String> rootEndpoint() {

        return ollamaService.chat(
                "Tell me a short funny story"
        );
    }
}
