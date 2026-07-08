package com.microsoft.samples.springopenai.data;

import lombok.Data;
import java.util.List;

@Data
public class ChatCompletionRequest {

    private String model;

    private List<Message> messages;

    private boolean stream;

}