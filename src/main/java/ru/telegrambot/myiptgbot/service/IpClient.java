package ru.telegrambot.myiptgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class IpClient {

    private final static String BASE_URI = "http://ipv4-internet.yandex.net";
    private final static String ENDPOINT = "/api/v0/ip";

    private final RestClient rest;

    public IpClient() {
        rest = RestClient.builder()
                .baseUrl(BASE_URI)
                .build();
    }

    public String getIp() {
        try {
            String body = rest.get().uri(ENDPOINT).retrieve().body(String.class);
            body = body.replace("\"", "");
            log.info("Ip received: {}.", body);
            return body;
        } catch (Exception e) {
            String errorMessage = "Could not receive IP:\n" + e.getMessage();
            log.error(errorMessage);
            return errorMessage;
        }
    }
}
