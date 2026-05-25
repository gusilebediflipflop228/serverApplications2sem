package com.ashaev.serverapps2.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для регистрации студента")
public record RegisterRequest(
        @Schema(description = "Имя пользователя", example = "ivanov_d")
        @NotBlank(message = "Логин обязателен")
        String username,

        @Schema(description = "Пароль", example = "secret123")
        @NotBlank(message = "Пароль обязателен")
        String password,

        @Schema(description = "Код учебной группы", example = "K-301")
        @NotBlank(message = "Код группы обязателен")
        String groupCode,

        @Schema(description = "ФИО студента", example = "Иванов Иван Иванович")
        @NotBlank(message = "ФИО обязательно")
        @JsonProperty("full_name")
        String fullName
) {}