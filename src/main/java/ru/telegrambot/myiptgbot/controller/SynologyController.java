package ru.telegrambot.myiptgbot.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.telegrambot.myiptgbot.service.NotificationService;

@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
public class SynologyController {

    private final NotificationService notificationService;

    @GetMapping("/notify")
    public void sendMessage(@RequestParam @NotBlank String text) {
        notificationService.notify(text);
    }
}
