package com.jinlei.aiassistant.provider;

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


@Service("ollama")
public class OllamaClient implements AIClient {


    private final WebClient client;


    @Value("${application.ollama.model}")
    private String model;


    public OllamaClient(
            WebClient.Builder builder,
            @Value("${application.ollama.url}") String url
    ) {
        this.client = builder
                .baseUrl(url)
                .build();
    }

    @Override
    public Flux<String> chat(String prompt) {

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