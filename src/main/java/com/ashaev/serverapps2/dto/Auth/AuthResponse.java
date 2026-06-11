package com.ashaev.serverapps2.dto.Auth;

import com.ashaev.serverapps2.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Ответ сервера с токенами авторизации")
public record AuthResponse(
        @Schema(description = "Токен доступа",
                example = "eyJhbGciOiJIUzI1NiJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String accessToken,

        @Schema(description = "Токен для обновления",
                example = "eyJhbGciOiJIUzI1NiJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String refreshToken,

        @Schema(description = "Роль пользователя", example = "ROLE_TEACHER",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Role role
) {
    @Override
    public String toString() {
        return "AuthResponse[" +
                "accessToken='<EXCLUDED>', " +
                "refreshToken='<EXCLUDED>', " +
                "role=" + role +
                ']';
    }
}