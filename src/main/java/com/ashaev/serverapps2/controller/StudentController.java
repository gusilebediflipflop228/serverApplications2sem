package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Student.StudentRequest;
import com.ashaev.serverapps2.dto.Student.StudentResponse;
import com.ashaev.serverapps2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "2. Студенты", description = "Управление информацией о студентах (CRUD)")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Добавить студента", description = "Создает профиль нового студента и привязывает его к группе.")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentService.createStudent(request)));
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Получить студентов группы", description = "Возвращает список всех студентов, учащихся в конкретной группе.")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByGroupId(
            @PathVariable @Parameter(description = "ID группы", example = "1") Long groupId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentsByGroupId(groupId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID", description = "Находит профиль студента по его уникальному идентификатору.")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @PathVariable @Parameter(description = "ID студента", example = "1") Long id) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getStudentById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные студента", description = "Позволяет изменить ФИО студента или перевести его в другую группу (поменяв groupId).")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable @Parameter(description = "ID студента", example = "1") Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentService.updateStudent(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить студента", description = "Удаляет запись о студенте из системы.")
    public ResponseEntity<ApiResponse<String>> deleteStudent(
            @PathVariable @Parameter(description = "ID студента", example = "1") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Студент успешно удален"));
    }
}