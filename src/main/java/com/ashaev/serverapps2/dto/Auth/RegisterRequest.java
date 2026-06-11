package com.ashaev.serverapps2.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для регистрации студента")
public record RegisterRequest(
        @Schema(description = "Имя пользователя", example = "ivanov_d", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Логин обязателен")
        @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
        String username,

        @Schema(description = "Пароль", example = "secret123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 128, message = "Пароль должен быть от 8 до 128 символов")
        String password,

        @Schema(description = "Код учебной группы", example = "K-301", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Код группы обязателен")
        @Size(min = 2, max = 20, message = "Некорректный код группы")
        String groupCode,

        @Schema(description = "ФИО студента", example = "Иванов Иван Иванович", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "ФИО обязательно")
        @Size(min = 5, max = 255, message = "ФИО должно быть от 5 до 255 символов")
        @JsonProperty("full_name")
        String fullName
) {}