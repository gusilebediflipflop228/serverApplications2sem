package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на обновление токена доступа")
public record RefreshTokenRequest(
        @Schema(description = "Валидный Refresh токен, полученный при входе",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFub3ZfZCIsImV4cCI6MTc4MDAwMDAwMH0...")
        @NotBlank(message = "Refresh токен обязателен")
        String refreshToken
) {}