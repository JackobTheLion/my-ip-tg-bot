package ru.telegrambot.myiptgbot.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Tag(name = "IP")
public interface IpApi {

    @Operation(summary = "Получить внешний IP-адрес сервера")
    @ApiResponse(responseCode = "200", description = "Внешний IP-адрес",
            content = @Content(mediaType = TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "503", description = "Не удалось получить IP-адрес",
            content = @Content(mediaType = TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    @GetMapping("/ip")
    String getIp();
}
