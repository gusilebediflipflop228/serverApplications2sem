package com.ashaev.serverapps2.dto.Auth;

import com.ashaev.serverapps2.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.ToString;

@Schema(description = "Ответ сервера с токенами авторизации")
public record AuthResponse(
        @Schema(description = "Короткоживущий токен доступа (Access Token). Передается в заголовке Authorization: Bearer <token>",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9URUFDSEVSIiwidXNlcklkIjoxLCJzdWIi...")
        @ToString.Exclude
        String accessToken,

        @Schema(description = "Долгоживущий токен для обновления (Refresh Token)",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFub3ZfZCIsImV4cCI6MTc4MDAwMDAwMH0...")
        @ToString.Exclude
        String refreshToken,

        @Schema(description = "Роль пользователя в системе", example = "ROLE_TEACHER")
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