package com.jinlei.aiassistant.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "application.ai")
public class AIProperties {


    @NotBlank
    private String provider;

    private Memory memory = new Memory();

    public static class Memory {

        @Min(1)
        private int maxMessages;

        public int getMaxMessages() {
            return maxMessages;
        }


        public void setMaxMessages(
                int maxMessages
        ) {
            this.maxMessages = maxMessages;
        }
    }
}