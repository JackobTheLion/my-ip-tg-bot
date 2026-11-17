package ru.telegrambot.myiptgbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.telegrambot.myiptgbot.config.properties.BotProperties;
import ru.telegrambot.myiptgbot.service.impl.TelegramBot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotInitializer {

    private final BotProperties botProperties;
    private final TelegramBot telegramBot;
    private final ScheduledExecutorService pollingExecutor = Executors.newSingleThreadScheduledExecutor();
    private final TelegramBotsLongPollingApplication pollingApplication = new TelegramBotsLongPollingApplication(
            ObjectMapper::new, new TelegramOkHttpClientFactory.DefaultOkHttpClientCreator(), () -> pollingExecutor);

    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        pollingApplication.registerBot(botProperties.token(), telegramBot);
        log.info("Telegram bot registered: {}", botProperties.name());
    }

    @PreDestroy
    public void close() throws Exception {
        try {
            pollingApplication.close();
        } finally {
            pollingExecutor.shutdownNow();
        }
    }
}
