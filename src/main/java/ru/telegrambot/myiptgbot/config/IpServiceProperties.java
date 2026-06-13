package ru.telegrambot.myiptgbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ip-service")
public record IpServiceProperties(String baseUrl, String endpoint) {
}
