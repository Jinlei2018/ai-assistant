package com.jinlei.aiassistant.service;

import com.jinlei.aiassistant.model.ollama.Message;
import com.jinlei.aiassistant.model.ollama.OllamaRequest;
import com.jinlei.aiassistant.model.ollama.OllamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
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


    public Flux<String> chatStream(String prompt) {

        OllamaRequest request = new OllamaRequest();

        request.setModel(model);
        request.setStream(true);
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", prompt));
        request.setMessages(messages);

        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(OllamaResponse.class)
                .filter(r -> r.getMessage() != null)
                .map(r -> r.getMessage().getContent())
                .filter(content -> content != null && !content.isBlank());
    }
}