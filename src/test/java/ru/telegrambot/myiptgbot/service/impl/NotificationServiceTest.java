package ru.telegrambot.myiptgbot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.telegrambot.myiptgbot.service.TelegramMessageSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private TelegramMessageSender messageSender;

    private TelegramNotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new TelegramNotificationService(messageSender);
    }

    @Test
    void notify_sendsMessage() {
        notificationService.notify("hello");
        verify(messageSender).sendMessage("hello");
    }

    @Test
    void notify_queuesOnFailure() {
        doThrow(new RuntimeException("fail")).when(messageSender).sendMessage("hello");
        notificationService.notify("hello");
        verify(messageSender).sendMessage("hello");
    }

    @Test
    void sendUnsent_retriesQueuedMessages() {
        doThrow(new RuntimeException("fail")).when(messageSender).sendMessage("hello");
        notificationService.notify("hello");

        notificationService.sendUnsent();
        verify(messageSender, times(2)).sendMessage("hello");
    }

    @Test
    void sendUnsent_doesNothingWhenQueueIsEmpty() {
        notificationService.sendUnsent();
        verify(messageSender, never()).sendMessage(anyString());
    }
}
