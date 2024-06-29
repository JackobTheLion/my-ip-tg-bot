package ru.telegrambot.myiptgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@Slf4j
public class IpClient {

    private final RestTemplate rest;

    public IpClient(RestTemplateBuilder builder) {
        rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory("http://ipv4-internet.yandex.net"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public String getIp() {
        String uri = "/api/v0/ip";
        String body = rest.getForEntity(uri, String.class).getBody();
        body = body.replace("\"", "");
        log.info("Ip received: {}.", body);
        return body;
    }
}
