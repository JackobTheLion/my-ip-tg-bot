package ru.telegrambot.myiptgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final TelegramBot telegramBot;
    private final List<String> unsentMessages = Collections.synchronizedList(new ArrayList<>());

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

    @Scheduled(cron = "${cron.retry-unsent}")
    public void sendUnsent() {
        List<String> toSend;
        synchronized (unsentMessages) {
            if (unsentMessages.isEmpty()) return;
            toSend = new ArrayList<>(unsentMessages);
            unsentMessages.clear();
        }
        log.info("{} unsent messages in queue", toSend.size());
        try {
            telegramBot.sendMessage("Sending delayed messages");
        } catch (Exception e) {
            log.error("Failed to send delayed message header", e);
        }
        for (String msg : toSend) {
            try {
                telegramBot.sendMessage(msg);
            } catch (Exception e) {
                log.error("Failed to send delayed message: {}", msg, e);
                synchronized (unsentMessages) {
                    unsentMessages.add(msg);
                }
            }
        }
    }
}
