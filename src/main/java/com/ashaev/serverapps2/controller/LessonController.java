package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Attendance.AttendanceUpdateRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonResponse;
import com.ashaev.serverapps2.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "5. Занятия и посещаемость", description = "Управление расписанием занятий и журналами посещаемости (CRUD)")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Создать новое занятие", description = "Преподаватель или Админ создает пару. Система автоматически находит всех студентов указанной группы и генерирует для них пустую ведомость.")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(@Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(ApiResponse.success(lessonService.createLesson(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Получить информацию о занятии и ведомость", description = "Возвращает данные занятия вместе со списком студентов и их статусом присутствия. Студенты видят только занятия своей группы.")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(
            @PathVariable @Parameter(description = "ID занятия", example = "1") Long id) {
        return ResponseEntity.ok(ApiResponse.success(lessonService.getLessonByIdWithCheck(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Редактировать параметры занятия", description = "Позволяет изменить дату, номер пары, предмет, группу или преподавателя для существующего занятия.")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(
            @PathVariable @Parameter(description = "ID занятия", example = "1") Long id,
            @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(ApiResponse.success(lessonService.updateLesson(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Удалить занятие из расписания", description = "Полностью удаляет занятие и связанную ведомость.")
    public ResponseEntity<ApiResponse<String>> deleteLesson(
            @PathVariable @Parameter(description = "ID занятия", example = "1") Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(ApiResponse.success("Занятие успешно удалено из расписания"));
    }

    @PutMapping("/{id}/attendance")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Отметить посещаемость на занятии", description = "Принимает список студентов с их статусами (пришел/не пришел) и массово обновляет ведомость для этого занятия.")
    public ResponseEntity<ApiResponse<String>> updateAttendance(
            @PathVariable @Parameter(description = "ID занятия", example = "1") Long id,
            @Valid @RequestBody List<AttendanceUpdateRequest> requests) {
        lessonService.updateAttendance(id, requests);
        return ResponseEntity.ok(ApiResponse.success("Данные о посещаемости успешно обновлены"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Получить расписание с фильтрами и пагинацией", description = "Позволяет выгрузить список пар за определенный период времени. Студенты всегда получают расписание только своей группы.")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "Начало периода (ГГГГ-ММ-ДД)", example = "2026-05-01") LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "Конец периода (ГГГГ-ММ-ДД)", example = "2026-05-31") LocalDate endDate,
            @RequestParam(required = false) @Parameter(description = "Фильтр по ID группы", example = "1") Long groupId,
            @RequestParam(required = false) @Parameter(description = "Фильтр по ID преподавателя", example = "1") Long teacherId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы (начиная с 0)", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество записей на странице", example = "10") int size) {

        return ResponseEntity.ok(ApiResponse.success(
                lessonService.getLessonsPagedWithCheck(startDate, endDate, groupId, teacherId, page, size)
        ));
    }
}