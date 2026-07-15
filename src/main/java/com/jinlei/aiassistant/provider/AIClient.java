package com.jinlei.aiassistant.provider;

import reactor.core.publisher.Flux;

public interface AIClient {

    Flux<String> chat(String prompt);

}