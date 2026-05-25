package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Запрос для регистрации администратора")
public record RegisterAdminRequest(
        @Schema(description = "Логин", example = "admin_super")
        @NotBlank
        String username,

        @Schema(description = "Пароль", example = "admin123")
        @NotBlank
        String password
) {}