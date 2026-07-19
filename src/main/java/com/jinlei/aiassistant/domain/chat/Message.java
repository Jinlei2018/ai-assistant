package com.jinlei.aiassistant.domain.chat;

public class Message {

    private String id;

    private String role;

    private String content;


    public Message(
            String role,
            String content
    ) {
        this.role = role;
        this.content = content;
    }


    public Message(
            String id,
            String role,
            String content
    ) {
        this.id = id;
        this.role = role;
        this.content = content;
    }


    public String getId() {
        return id;
    }


    public String getRole() {
        return role;
    }


    public String getContent() {
        return content;
    }
}