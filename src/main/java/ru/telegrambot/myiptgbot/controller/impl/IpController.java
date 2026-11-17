package ru.telegrambot.myiptgbot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.telegrambot.myiptgbot.controller.api.IpApi;
import ru.telegrambot.myiptgbot.service.IpClient;

@RestController
@RequiredArgsConstructor
public class IpController implements IpApi {

    private final IpClient ipClient;

    @Override
    public String getIp() {
        return ipClient.getIp();
    }
}
