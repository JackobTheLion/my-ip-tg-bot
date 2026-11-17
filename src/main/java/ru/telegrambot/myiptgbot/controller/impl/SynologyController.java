package ru.telegrambot.myiptgbot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.telegrambot.myiptgbot.controller.api.SynologyApi;
import ru.telegrambot.myiptgbot.service.NotificationService;

@Validated
@RequiredArgsConstructor
@RestController
public class SynologyController implements SynologyApi {

    private final NotificationService notificationService;

    @Override
    public void sendMessage(String text) {
        notificationService.notify(text);
    }
}
