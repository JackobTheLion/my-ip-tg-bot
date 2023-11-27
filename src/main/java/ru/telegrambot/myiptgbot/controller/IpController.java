package ru.telegrambot.myiptgbot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.telegrambot.myiptgbot.service.IpClient;

@RestController
@Slf4j
@AllArgsConstructor
public class IpController {

    private final IpClient ipClient;

    @GetMapping("/ip")
    public String getIp() {
        return ipClient.getIp();
    }

}
