package ru.telegrambot.myiptgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final TelegramBot telegramBot;
    private List<String> unsentMessages = new ArrayList<>();

    public void notify(String message) {
        log.info("Message received: {}", message);
        try {
            log.info("Sending message");
            telegramBot.sendMessage(message);
        } catch (Exception e) {
            log.error("Could not send message to Telegram. Reason: {}", e.getMessage());
            unsentMessages.add(message);
        }
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void sendUnsent() {
        if (!unsentMessages.isEmpty()) {
            log.info("{} unsent messages in queue", unsentMessages.size());
            telegramBot.sendMessage("Отправляем отложенные сообщения");
            unsentMessages.forEach(telegramBot::sendMessage);
            unsentMessages = new ArrayList<>();
        }
    }
}
