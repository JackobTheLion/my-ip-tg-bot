package ru.telegrambot.myiptgbot.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.telegrambot.myiptgbot.exception.IpClientException;
import ru.telegrambot.myiptgbot.service.IpClient;
import ru.telegrambot.myiptgbot.service.TelegramMessageSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IpMonitoringServiceTest {

    @Mock
    private IpClient ipClient;

    @Mock
    private TelegramMessageSender messageSender;

    @InjectMocks
    private IpMonitoringService monitoringService;

    @Test
    void notifiesOnceWhenIpChanges() {
        when(ipClient.getIp()).thenReturn("192.0.2.1", "192.0.2.1", "192.0.2.2");

        monitoringService.initializeIp();
        monitoringService.checkIp();
        verifyNoInteractions(messageSender);

        monitoringService.checkIp();
        monitoringService.checkIp();
        verify(messageSender).sendMessage("Ip was updated. New IP is 192.0.2.2");
        verifyNoMoreInteractions(messageSender);
    }

    @Test
    void notifiesWhenTheFirstScheduledLookupSucceedsAfterStartupFailure() {
        when(ipClient.getIp())
                .thenThrow(new IpClientException("Unavailable"))
                .thenReturn("192.0.2.1");

        monitoringService.initializeIp();
        verifyNoInteractions(messageSender);

        monitoringService.checkIp();
        verify(messageSender).sendMessage("Ip was updated. New IP is 192.0.2.1");
    }
}
