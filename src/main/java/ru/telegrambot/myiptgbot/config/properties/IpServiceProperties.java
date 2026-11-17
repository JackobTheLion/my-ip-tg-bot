package ru.telegrambot.myiptgbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ip-service")
public record IpServiceProperties(
        String baseUrl,
        String endpoint
) {
}
