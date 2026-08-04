package com.jinlei.aiassistant.domain.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Conversation {

    private String summary;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {

        messages.add(message);
    }


}