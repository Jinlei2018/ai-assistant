package com.jinlei.aiassistant.provider;

import com.jinlei.aiassistant.domain.chat.Message;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AIClient {

    Flux<String> chat(List<Message> messages);

}