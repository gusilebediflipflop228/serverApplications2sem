package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Discipline.DisciplineRequest;
import com.ashaev.serverapps2.dto.Discipline.DisciplineResponse;
import com.ashaev.serverapps2.service.DisciplineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/disciplines")
@RequiredArgsConstructor
@Tag(name = "4. Дисциплины", description = "Управление каталогом учебных предметов (CRUD)")
public class DisciplineController {

    private final DisciplineService disciplineService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать дисциплину", description = "Добавляет новый учебный предмет в базу данных. Доступно только Администраторам.")
    public ResponseEntity<ApiResponse<DisciplineResponse>> createDiscipline(@Valid @RequestBody DisciplineRequest request) {
        return ResponseEntity.ok(ApiResponse.success(disciplineService.createDiscipline(request)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Получить все дисциплины", description = "Выгружает полный список доступных предметов. Доступно всем авторизованным пользователям.")
    public ResponseEntity<ApiResponse<List<DisciplineResponse>>> getAllDisciplines() {
        return ResponseEntity.ok(ApiResponse.success(disciplineService.getAllDisciplines()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Получить дисциплину по ID", description = "Ищет предмет в каталоге по его уникальному идентификатору. Доступно всем авторизованным пользователям.")
    public ResponseEntity<ApiResponse<DisciplineResponse>> getDisciplineById(
            @PathVariable @Parameter(description = "ID дисциплины", example = "1") Long id) {
        return ResponseEntity.ok(ApiResponse.success(disciplineService.getDisciplineById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить название дисциплины", description = "Позволяет изменить наименование существующего предмета. Доступно только Администраторам.")
    public ResponseEntity<ApiResponse<DisciplineResponse>> updateDiscipline(
            @PathVariable @Parameter(description = "ID дисциплины", example = "1") Long id,
            @Valid @RequestBody DisciplineRequest request) {
        return ResponseEntity.ok(ApiResponse.success(disciplineService.updateDiscipline(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить дисциплину", description = "Удаляет предмет из каталога по его ID. Доступно только Администраторам.")
    public ResponseEntity<ApiResponse<String>> deleteDiscipline(
            @PathVariable @Parameter(description = "ID дисциплины", example = "1") Long id) {
        disciplineService.deleteDiscipline(id);
        return ResponseEntity.ok(ApiResponse.success("Дисциплина успешно удалена"));
    }
}