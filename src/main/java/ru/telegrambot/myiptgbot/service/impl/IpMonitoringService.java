package ru.telegrambot.myiptgbot.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.telegrambot.myiptgbot.service.IpClient;
import ru.telegrambot.myiptgbot.service.TelegramMessageSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpMonitoringService {

    private final IpClient ipClient;
    private final TelegramMessageSender messageSender;
    private volatile String lastKnownIp;

    @PostConstruct
    void initializeIp() {
        try {
            lastKnownIp = ipClient.getIp();
        } catch (Exception e) {
            log.warn("Could not fetch initial IP, will retry on schedule", e);
        }
    }

    @Scheduled(cron = "${cron.ip-check}")
    void checkIp() {
        log.info("Checking IP");
        try {
            String currentIp = ipClient.getIp();
            if (currentIp.equals(lastKnownIp)) {
                log.info("IP {} did not change", currentIp);
                return;
            }

            log.info("IP updated: {}", currentIp);
            lastKnownIp = currentIp;
            messageSender.sendMessage("Ip was updated. New IP is " + currentIp);
        } catch (Exception e) {
            log.error("Failed to check IP", e);
        }
    }
}
