package com.jinlei.aiassistant.provider;

import com.jinlei.aiassistant.config.AIProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AIClientFactory {

    private final Map<String, AIClient> clients;
    private final AIProperties properties;

    public AIClientFactory(
            Map<String, AIClient> clients,
            AIProperties properties
    ) {
        this.clients = clients;
        this.properties = properties;
    }

    public AIClient getClient() {

        AIClient client = clients.get(properties.getProvider());

        if (client == null) {
            throw new IllegalArgumentException(
                    "Unknown AI provider: "
                            + properties.getProvider()
            );
        }

        return client;
    }

}