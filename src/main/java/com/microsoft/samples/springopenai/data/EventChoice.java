package com.microsoft.samples.springopenai.data;

public class EventChoice {

    private Delta delta;

    private String finishReason;


    public Delta getDelta() {
        return delta;
    }


    public void setDelta(Delta delta) {
        this.delta = delta;
    }


    public String getFinishReason() {
        return finishReason;
    }


    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}