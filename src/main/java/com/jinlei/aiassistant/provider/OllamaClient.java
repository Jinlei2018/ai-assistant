package com.jinlei.aiassistant.provider;

import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.domain.ollama.OllamaRequest;
import com.jinlei.aiassistant.domain.ollama.OllamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
    public Flux<String> chat(List<Message> chatMessages) {

        OllamaRequest request = new OllamaRequest();

        request.setModel(model);
        request.setStream(true);

        List<com.jinlei.aiassistant.domain.ollama.Message> messages = chatMessages.stream()
                .map(message ->
                        new com.jinlei.aiassistant.domain.ollama.Message(
                                message.getRole(),
                                message.getContent()
                        )
                )
                .toList();

        request.setMessages(messages);


        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(OllamaResponse.class)
                .filter(response ->
                        response.getMessage() != null
                )
                .map(response ->
                        response.getMessage().getContent()
                );
    }
}