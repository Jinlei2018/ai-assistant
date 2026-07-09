package com.jinlei.aiassistant.model.openai;

import lombok.Data;

@Data
public class Message {

    private String role;

    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
}