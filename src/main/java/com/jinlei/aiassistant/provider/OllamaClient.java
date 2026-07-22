package com.jinlei.aiassistant.provider;

import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.dto.ollama.OllamaMessage;
import com.jinlei.aiassistant.dto.ollama.OllamaRequest;
import com.jinlei.aiassistant.dto.ollama.OllamaResponse;
import com.jinlei.aiassistant.mapper.OllamaMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;


@Service("ollama")
public class OllamaClient implements AIClient {


    private final WebClient client;
    private final OllamaMapper mapper;


    @Value("${application.ollama.model}")
    private String model;


    public OllamaClient(
            WebClient.Builder builder,
            @Value("${application.ollama.url}") String url, OllamaMapper mapper
    ) {
        this.mapper = mapper;
        this.client = builder
                .baseUrl(url)
                .build();
    }

    @Override
    public Flux<String> chat(List<Message> chatMessages) {
        OllamaRequest request =
                mapper.toRequest(
                        model,
                        chatMessages,
                        true
                );

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