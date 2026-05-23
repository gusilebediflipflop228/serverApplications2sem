package com.ashaev.serverapps2.dto.Lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Запрос на создание нового занятия")
public class LessonRequest {

    @NotNull(message = "ID дисциплины обязателен")
    @Schema(description = "Уникальный идентификатор дисциплины (предмета)", example = "1")
    private Long disciplineId;

    @NotNull(message = "ID группы обязателен")
    @Schema(description = "Уникальный идентификатор учебной группы", example = "1")
    private Long groupId;

    @NotNull(message = "ID преподавателя обязателен")
    @Schema(description = "Уникальный идентификатор преподавателя", example = "1")
    private Long teacherId;

    @NotNull(message = "Дата занятия обязательна")
    @Schema(description = "Дата проведения занятия (ГГГГ-ММ-ДД)", example = "2026-05-23")
    private LocalDate classDate;

    @NotNull(message = "Номер пары обязателен")
    @Schema(description = "Номер пары по расписанию (от 1 до 8)", example = "1")
    private Integer classNumber;
}