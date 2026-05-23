package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Teacher.TeacherRequest;
import com.ashaev.serverapps2.dto.Teacher.TeacherResponse;
import com.ashaev.serverapps2.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Tag(name = "3. Преподаватели", description = "Управление составом преподавателей (CRUD)")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @Operation(summary = "Добавить преподавателя", description = "Вносит нового преподавателя в систему. Проверяет уникальность ФИО.")
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.createTeacher(request)));
    }

    @GetMapping
    @Operation(summary = "Получить список преподавателей (постранично)", description = "Возвращает список преподавателей вуза с поддержкой пагинации.")
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAllTeachers(
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы (с 0)", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов на странице", example = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getAllTeachersPaged(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить преподавателя по ID", description = "Ищет преподавателя в базе данных по его уникальному идентификатору.")
    public ResponseEntity<ApiResponse<TeacherResponse>> getTeacherById(
            @PathVariable @Parameter(description = "ID преподавателя", example = "1") Long id) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeacherById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные преподавателя", description = "Позволяет изменить ФИО преподавателя по его ID. Также проверяет ФИО на уникальность.")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(
            @PathVariable @Parameter(description = "ID преподавателя", example = "1") Long id,
            @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.updateTeacher(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить преподавателя", description = "Удаляет запись о преподавателе из системы по его ID.")
    public ResponseEntity<ApiResponse<String>> deleteTeacher(
            @PathVariable @Parameter(description = "ID преподавателя", example = "1") Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.success("Преподаватель успешно удален"));
    }
}