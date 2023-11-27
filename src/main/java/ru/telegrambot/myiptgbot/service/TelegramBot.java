package ru.telegrambot.myiptgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String name;

    private final IpClient ipClient;

    private final Long botAdminChatId;

    public TelegramBot(@Value("${bot.name}") String name, @Value("${bot.token}") String token,
                       @Value("${bot.AdminChatId}") Long botAdminChatId, @Autowired IpClient ipClient) {
        super(token);
        this.name = name;
        this.botAdminChatId = botAdminChatId;
        this.ipClient = ipClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String messageToSend;
        if (update.hasMessage() && update.getMessage().hasText() && isAuthorized(update)) {
            String incomingMessage = update.getMessage().getText();
            if (incomingMessage.equals("/ip")) {
                messageToSend = ipClient.getIp();
            } else {
                messageToSend = "Unknown command. Please try again";
            }
        } else {
            messageToSend = String.format("User %s tried to get ip. Message sent: '%s'",
                    update.getMessage().getChat().getFirstName(), update.getMessage().getText());
        }
        sendMessage(messageToSend);
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    private void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(botAdminChatId), message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAuthorized(Update update) {
        Long chatId = update.getMessage().getChatId();
        return chatId.equals(botAdminChatId);
    }
}
