package ru.telegrambot.myiptgbot.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.telegrambot.myiptgbot.config.properties.BotProperties;
import ru.telegrambot.myiptgbot.service.IpClient;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    private static final long ADMIN_CHAT_ID = 12345L;

    @Mock
    private IpClient ipClient;

    @Mock
    private TelegramClient telegramClient;

    @Test
    void repliesToIpCommandReceivedByLongPolling() throws TelegramApiException {
        when(ipClient.getIp()).thenReturn("192.0.2.1");

        Message command = new Message();
        command.setChat(Chat.builder().id(ADMIN_CHAT_ID).type("private").build());
        command.setText("/ip");
        Update update = new Update();
        update.setMessage(command);

        BotProperties botProperties = new BotProperties("test_bot", "test_token", ADMIN_CHAT_ID);
        try (TelegramBot bot = new TelegramBot(botProperties, ipClient, telegramClient)) {
            bot.consume(List.of(update));

            verify(telegramClient, timeout(2000)).execute(SendMessage.builder()
                    .chatId(ADMIN_CHAT_ID)
                    .text("192.0.2.1")
                    .build());
        }
    }
}
