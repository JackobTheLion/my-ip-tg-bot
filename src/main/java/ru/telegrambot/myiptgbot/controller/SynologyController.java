package ru.telegrambot.myiptgbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.telegrambot.myiptgbot.service.NotificationService;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SynologyController {

    private final NotificationService notificationService;

    @GetMapping("/notify")
    private void sendMessage(@RequestParam String text) {
        notificationService.notify(text);
    }
}
