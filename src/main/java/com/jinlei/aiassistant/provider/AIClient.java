package com.jinlei.aiassistant.provider;

import com.jinlei.aiassistant.model.chat.ChatMessage;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AIClient {

    Flux<String> chat(List<ChatMessage> messages);

}