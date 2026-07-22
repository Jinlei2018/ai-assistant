package com.jinlei.aiassistant.mapper;

import com.jinlei.aiassistant.domain.chat.Message;
import com.jinlei.aiassistant.dto.ollama.OllamaMessage;
import com.jinlei.aiassistant.dto.ollama.OllamaRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OllamaMapper {

    public OllamaRequest toRequest(
            String model,
            List<Message> messages,
            boolean stream
    ) {

        OllamaRequest request = new OllamaRequest();

        request.setModel(model);
        request.setStream(stream);

        request.setMessages(
                messages.stream()
                        .map(this::toDto)
                        .toList()
        );

        return request;
    }

    public OllamaMessage toDto(
            Message message
    ) {
        return new OllamaMessage(
                message.getRole(),
                message.getContent()
        );
    }

}