package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос для регистрации администратора")
public record RegisterAdminRequest(
        @Schema(description = "Логин", example = "admin_super", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Логин обязателен")
        @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
        String username,

        @Schema(description = "Пароль", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 128, message = "Пароль должен быть от 8 до 128 символов")
        String password
) {}