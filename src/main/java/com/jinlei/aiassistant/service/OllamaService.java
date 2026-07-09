package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.model.ollama.Message;
import com.jinlei.aiassistant.model.ollama.OllamaRequest;
import com.jinlei.aiassistant.model.ollama.OllamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class OllamaService {


    private final WebClient client;


    @Value("${application.ollama.model}")
    private String model;


    public OllamaService(
            WebClient.Builder builder,
            @Value("${application.ollama.url}") String url
    ) {
        this.client = builder
                .baseUrl(url)
                .build();
    }


    public Mono<String> chat(String prompt) {


        OllamaRequest request = new OllamaRequest();

        request.setModel(model);

        request.setMessages(
                List.of(
                        new Message(
                                "user",
                                prompt
                        )
                )
        );

        request.setStream(false);


        return client.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .map(response ->
                        response.getMessage().getContent()
                );
    }
}