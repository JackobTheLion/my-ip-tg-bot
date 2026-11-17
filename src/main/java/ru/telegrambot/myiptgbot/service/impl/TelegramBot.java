package ru.telegrambot.myiptgbot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.telegrambot.myiptgbot.config.properties.BotProperties;
import ru.telegrambot.myiptgbot.service.IpClient;
import ru.telegrambot.myiptgbot.service.TelegramMessageSender;

@Component
@Slf4j
public class TelegramBot extends DefaultLongPollingUpdateConsumer implements TelegramMessageSender {

    private final IpClient ipClient;
    private final TelegramClient telegramClient;
    private final Long botAdminChatId;

    public TelegramBot(BotProperties botProperties, IpClient ipClient, TelegramClient telegramClient) {
        this.botAdminChatId = botProperties.adminChatId();
        this.ipClient = ipClient;
        this.telegramClient = telegramClient;
    }

    @Override
    public void consume(Update update) {
        try {
            sendMessage(replyFor(update));
        } catch (Exception e) {
            log.error("Failed to process update", e);
        }
    }

    private String replyFor(Update update) {
        if (!update.hasMessage()) {
            return "Received non-message update";
        }

        Message message = update.getMessage();
        if (!message.hasText() || !botAdminChatId.equals(message.getChatId())) {
            return String.format("User %s tried to get ip. Message sent: '%s'",
                    message.getChat().getFirstName(), message.getText());
        }
        if (!"/ip".equals(message.getText())) {
            return "Unknown command. Please try again";
        }

        try {
            return ipClient.getIp();
        } catch (Exception e) {
            return "Could not retrieve IP: " + e.getMessage();
        }
    }

    @Override
    public void sendMessage(String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(botAdminChatId)
                .text(message)
                .build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }
}
