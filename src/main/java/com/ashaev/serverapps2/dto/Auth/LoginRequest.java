package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для аутентификации пользователя")
public record LoginRequest(
        @Schema(description = "Имя пользователя (логин)", example = "ivanov_d")
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String username,

        @Schema(description = "Пароль пользователя", example = "secretPass123")
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}