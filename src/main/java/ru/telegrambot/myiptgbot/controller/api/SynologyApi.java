package ru.telegrambot.myiptgbot.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Tag(name = "Synology")
public interface SynologyApi {

    @Operation(summary = "Передать уведомление Synology в Telegram")
    @ApiResponse(responseCode = "200", description = "Уведомление принято", content = @Content)
    @ApiResponse(responseCode = "400", description = "Параметр text пустой",
            content = @Content(mediaType = TEXT_PLAIN_VALUE, schema = @Schema(type = "string")))
    @GetMapping("/notify")
    void sendMessage(@Parameter(description = "Текст уведомления") @RequestParam("text") @NotBlank String text);
}
