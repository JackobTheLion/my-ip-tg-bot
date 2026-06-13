package ru.telegrambot.myiptgbot.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.telegrambot.myiptgbot.config.BotProperties;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String name;
    private final IpClient ipClient;
    private final Long botAdminChatId;
    private volatile String currentIp;

    public TelegramBot(BotProperties botProperties, IpClient ipClient) {
        super(botProperties.token());
        this.name = botProperties.name();
        this.botAdminChatId = botProperties.adminChatId();
        this.ipClient = ipClient;
    }

    @PostConstruct
    public void init() {
        try {
            currentIp = ipClient.getIp();
        } catch (Exception e) {
            log.warn("Could not fetch initial IP, will retry on schedule", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String messageToSend;
            if (update.hasMessage() && update.getMessage().hasText() && isAuthorized(update)) {
                String incomingMessage = update.getMessage().getText();
                if (incomingMessage.equals("/ip")) {
                    try {
                        messageToSend = ipClient.getIp();
                    } catch (Exception e) {
                        messageToSend = "Could not retrieve IP: " + e.getMessage();
                    }
                } else {
                    messageToSend = "Unknown command. Please try again";
                }
            } else if (update.hasMessage()) {
                messageToSend = String.format("User %s tried to get ip. Message sent: '%s'",
                        update.getMessage().getChat().getFirstName(), update.getMessage().getText());
            } else {
                messageToSend = "Received non-message update";
            }
            sendMessage(messageToSend);
        } catch (Exception e) {
            log.error("Failed to process update", e);
        }
    }

    @Scheduled(cron = "${cron.ip-check}")
    public void updateIp() {
        log.info("Checking ip...");
        try {
            String ipReceived = ipClient.getIp();
            if (!ipReceived.equals(currentIp)) {
                log.info("Ip updated: {}", ipReceived);
                currentIp = ipReceived;
                sendMessage("Ip was updated. New IP is " + ipReceived);
            } else {
                log.info("IP {} did not change", currentIp);
            }
        } catch (Exception e) {
            log.error("Failed to check IP", e);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(botAdminChatId), message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }

    private boolean isAuthorized(Update update) {
        Long chatId = update.getMessage().getChatId();
        return chatId.equals(botAdminChatId);
    }
}
