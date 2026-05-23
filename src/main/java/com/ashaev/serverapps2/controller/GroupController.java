package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.ApiResponse;
import com.ashaev.serverapps2.dto.Group.GroupRequest;
import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "1. Учебные группы", description = "Управление академическими группами (CRUD)")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Создать новую группу", description = "Добавляет новую учебную группу в базу данных.")
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(@Valid @RequestBody GroupRequest request) {
        return ResponseEntity.ok(ApiResponse.success(groupService.createGroup(request)));
    }

    @GetMapping
    @Operation(summary = "Получить список всех групп", description = "Возвращает полный список зарегистрированных групп.")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getAllGroups() {
        return ResponseEntity.ok(ApiResponse.success(groupService.getAllGroups()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить группу по ID", description = "Ищет учебную группу по её уникальному идентификатору.")
    public ResponseEntity<ApiResponse<GroupResponse>> getGroupById(
            @PathVariable @Parameter(description = "ID группы", example = "1") Long id) {
        return ResponseEntity.ok(ApiResponse.success(groupService.getGroupById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные группы", description = "Позволяет изменить название существующей учебной группы.")
    public ResponseEntity<ApiResponse<GroupResponse>> updateGroup(
            @PathVariable @Parameter(description = "ID группы", example = "1") Long id,
            @Valid @RequestBody GroupRequest request) {
        return ResponseEntity.ok(ApiResponse.success(groupService.updateGroup(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить группу", description = "Удаляет учебную группу из системы. Убедитесь, что в группе нет привязанных студентов перед удалением!")
    public ResponseEntity<ApiResponse<String>> deleteGroup(
            @PathVariable @Parameter(description = "ID группы", example = "1") Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok(ApiResponse.success("Учебная группа успешно удалена"));
    }
}