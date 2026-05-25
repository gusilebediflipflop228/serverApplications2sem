package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Auth.*;
import com.ashaev.serverapps2.entity.User;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "0. Аутентификация", description = "Методы входа в систему")
public class AuthController {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Войти в систему", description = "Возвращает JWT-токен для авторизации")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/register/student")
    @Operation(summary = "Регистрация нового пользователя/студента", description = "Регистрирует нового пользователя с ролью STUDENT. Доступно для всех")
    public ResponseEntity<AuthResponse> registerStudent(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerStudent(request));
    }

    @PostMapping("/register/teacher")
    @Operation(summary = "Регистрация нового пользователя/преподавателя", description = "Регистрирует нового пользователя с ролью TEACHER. Доступно только для администраторов")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerTeacher(@RequestBody RegisterTeacherRequest request) {
        return ResponseEntity.ok(authService.registerTeacher(request));
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Регистрация нового пользователя/администратора", description = "Регистрирует нового пользователя с ролью ADMIN. Доступно только для администраторов")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterAdminRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/test-reset-admin")
    public String resetAdmin() {
        User admin = userRepository.findByUsername("admin")
                .orElseThrow(() -> new RuntimeException("Админ не найден"));

        admin.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(admin);
        return "Пароль админа успешно обновлен через твой Encoder!";
    }
}