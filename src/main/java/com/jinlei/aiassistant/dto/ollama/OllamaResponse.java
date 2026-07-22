package com.jinlei.aiassistant.dto.ollama;

public class OllamaResponse {

    private OllamaMessage message;
    private boolean done;

    public OllamaMessage getMessage() {
        return message;
    }

    public void setMessage(OllamaMessage message) {
        this.message = message;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}