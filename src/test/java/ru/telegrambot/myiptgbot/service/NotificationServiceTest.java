package ru.telegrambot.myiptgbot.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private TelegramBot telegramBot;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(telegramBot);
    }

    @Test
    void notify_sendsMessage() {
        notificationService.notify("hello");
        verify(telegramBot).sendMessage("hello");
    }

    @Test
    void notify_queuesOnFailure() {
        doThrow(new RuntimeException("fail")).when(telegramBot).sendMessage("hello");
        notificationService.notify("hello");
        verify(telegramBot).sendMessage("hello");
    }

    @Test
    void sendUnsent_retriesQueuedMessages() {
        doThrow(new RuntimeException("fail")).when(telegramBot).sendMessage("hello");
        notificationService.notify("hello");

        notificationService.sendUnsent();
        verify(telegramBot, times(2)).sendMessage("hello");
    }

    @Test
    void sendUnsent_doesNothingWhenQueueIsEmpty() {
        notificationService.sendUnsent();
        verify(telegramBot, never()).sendMessage(anyString());
    }
}
