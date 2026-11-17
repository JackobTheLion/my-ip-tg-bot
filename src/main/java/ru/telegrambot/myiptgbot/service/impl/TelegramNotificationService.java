package ru.telegrambot.myiptgbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.telegrambot.myiptgbot.service.NotificationService;
import ru.telegrambot.myiptgbot.service.TelegramMessageSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService implements NotificationService {

    private final TelegramMessageSender messageSender;
    private final List<String> unsentMessages = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void notify(String message) {
        log.info("Message received: {}", message);
        try {
            log.info("Sending message");
            messageSender.sendMessage(message);
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
            messageSender.sendMessage("Sending delayed messages");
        } catch (Exception e) {
            log.error("Failed to send delayed message header", e);
        }
        for (String msg : toSend) {
            try {
                messageSender.sendMessage(msg);
            } catch (Exception e) {
                log.error("Failed to send delayed message: {}", msg, e);
                synchronized (unsentMessages) {
                    unsentMessages.add(msg);
                }
            }
        }
    }
}
