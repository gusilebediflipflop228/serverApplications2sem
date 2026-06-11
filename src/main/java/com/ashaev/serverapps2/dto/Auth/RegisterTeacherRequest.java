package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для регистрации учителя")
public record RegisterTeacherRequest(
        @Schema(description = "Имя пользователя", example = "ivanov_d", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Логин обязателен")
        @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
        String username,

        @Schema(description = "Пароль", example = "secret123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 128, message = "Пароль должен быть от 8 до 128 символов")
        String password,

        @Schema(description = "ФИО учителя", example = "Иванов Иван Иванович", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ФИО обязательно")
        @Size(min = 5, max = 255, message = "ФИО должно быть от 5 до 255 символов")
        String fullName
) {}