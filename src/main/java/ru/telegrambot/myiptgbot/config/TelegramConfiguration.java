package ru.telegrambot.myiptgbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.telegrambot.myiptgbot.config.properties.BotProperties;

@Configuration
public class TelegramConfiguration {

    @Bean
    public TelegramClient telegramClient(BotProperties botProperties) {
        return new OkHttpTelegramClient(botProperties.token());
    }
}
