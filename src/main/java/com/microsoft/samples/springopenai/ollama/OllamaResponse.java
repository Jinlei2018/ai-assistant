package com.microsoft.samples.springopenai.ollama;

public class OllamaResponse {

    private Message message;


    public Message getMessage() {
        return message;
    }


    public void setMessage(Message message) {
        this.message = message;
    }
}