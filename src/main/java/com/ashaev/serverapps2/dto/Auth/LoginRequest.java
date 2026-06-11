package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для аутентификации пользователя")
public record LoginRequest(
        @Schema(description = "Имя пользователя (логин)", example = "ivanov_d", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
        String username,

        @Schema(description = "Пароль пользователя", example = "secretPass123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 128, message = "Пароль должен быть от 8 до 128 символов")
        String password
) {}