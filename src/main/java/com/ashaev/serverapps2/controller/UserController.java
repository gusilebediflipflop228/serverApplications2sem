package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "2. Пользователи", description = "Методы для работы с профилем")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Получить данные о текущем пользователе")
    public ResponseEntity<String> getCurrentUser() {
        return ResponseEntity.ok("Ты успешно авторизован!");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Доступно только администраторам")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь успешно удален");
    }
}