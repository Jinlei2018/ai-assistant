package com.jinlei.aiassistant.domain.ollama;

public class OllamaResponse {

    private Message message;
    private boolean done;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}