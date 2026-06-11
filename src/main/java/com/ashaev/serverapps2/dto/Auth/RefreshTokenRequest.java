package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление токена доступа")
public record RefreshTokenRequest(
        @Schema(description = "Валидный Refresh токен, полученный при входе",
                example = "eyJhbGciOiJIUzI1NiJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Refresh токен обязателен")
        @Size(min = 32, max = 2048, message = "Некорректная длина токена")
        String refreshToken
) {}