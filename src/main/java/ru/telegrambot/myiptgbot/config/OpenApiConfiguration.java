package ru.telegrambot.myiptgbot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "My IP Telegram Bot API", version = "1.0"))
public class OpenApiConfiguration {
}
