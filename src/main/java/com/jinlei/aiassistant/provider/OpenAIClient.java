package com.jinlei.aiassistant.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.jinlei.aiassistant.model.openai.ChatCompletionRequest;
import com.jinlei.aiassistant.model.openai.EventData;
import com.jinlei.aiassistant.model.openai.Message;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OpenAIClient {

    private final Log log = LogFactory.getLog(OpenAIClient.class);

    private final String prompt = """
                Hello, introduce yourself in one sentence.
                """;

    @Value("${application.openai.url}")
    private String openAiUrl;

    @Value("${application.openai.key}")
    private String openAiKey;

    private WebClient client;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );

    @PostConstruct
    public void init() {
        client = WebClient.builder()
                .baseUrl(openAiUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + openAiKey
                )
                .build();
    }

    public Flux<String> getData() throws JsonProcessingException {
        ChatCompletionRequest request =
                new ChatCompletionRequest();

        request.setModel("gpt-4o-mini");

        request.setMessages(
                List.of(
                        new Message(
                                "user",
                                prompt
                        )
                )
        );

        request.setStream(true);

        String requestValue = objectMapper.writeValueAsString(request);

        return client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .onStatus(
                        status -> status.value() >= 400,
                        response ->
                                response.bodyToMono(String.class)
                                        .flatMap(body ->
                                                Mono.error(
                                                        new RuntimeException(
                                                                "OpenAI error: " + body
                                                        )
                                                )
                                        )
                )
                .bodyToFlux(String.class)
                .mapNotNull(event -> {
                    try {
                        String jsonData = event.substring(event.indexOf("{"), event.lastIndexOf("}") + 1);
                        return objectMapper.readValue(jsonData, EventData.class);
                    } catch (JsonProcessingException | StringIndexOutOfBoundsException e) {
                        return null;
                    }
                }).skipUntil(event ->
                        event.getChoices()
                                .get(0)
                                .getDelta()
                                .getContent() != null
                ).mapNotNull(event ->
                        event.getChoices()
                                .get(0)
                                .getDelta()
                                .getContent()
                );
    }
}
