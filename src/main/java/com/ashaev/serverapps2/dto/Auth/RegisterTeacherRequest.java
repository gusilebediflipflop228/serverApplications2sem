package com.ashaev.serverapps2.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос для регистрации учителя")
public record RegisterTeacherRequest(
        @Schema(description = "Имя пользователя", example = "ivanov_d")
        String username,

        @Schema(description = "Пароль", example = "secret123")
        String password,

        @Schema(description = "ФИО учителя", example = "Иванов Иван Иванович")
        String fullName
) {}