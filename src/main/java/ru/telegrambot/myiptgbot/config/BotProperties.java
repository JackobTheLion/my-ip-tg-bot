package ru.telegrambot.myiptgbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotProperties(String name, String token, Long adminChatId) {
}
