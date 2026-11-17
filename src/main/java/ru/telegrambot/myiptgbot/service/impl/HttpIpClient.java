package ru.telegrambot.myiptgbot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.telegrambot.myiptgbot.config.properties.IpServiceProperties;
import ru.telegrambot.myiptgbot.exception.IpClientException;
import ru.telegrambot.myiptgbot.service.IpClient;

@Service
@Slf4j
public class HttpIpClient implements IpClient {

    private final RestClient rest;
    private final String endpoint;

    public HttpIpClient(IpServiceProperties properties) {
        this.rest = RestClient.builder().baseUrl(properties.baseUrl()).build();
        this.endpoint = properties.endpoint();
    }

    @Override
    public String getIp() {
        try {
            String body = rest.get().uri(endpoint).retrieve().body(String.class);
            if (body == null) {
                throw new IpClientException("Empty response from IP service");
            }
            body = body.replace("\"", "");
            log.info("Ip received: {}", body);
            return body;
        } catch (RestClientException e) {
            throw new IpClientException("Could not receive IP", e);
        }
    }
}
