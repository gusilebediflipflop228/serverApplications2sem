package com.ashaev.serverapps2.dto.Attendance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Данные для отметки присутствия конкретного студента")
public class AttendanceUpdateRequest {

    @NotNull(message = "ID студента обязателен")
    @Schema(description = "Уникальный идентификатор студента", example = "1")
    private Long studentId;

    @NotNull(message = "Статус присутствия обязателен")
    @Schema(description = "Флаг присутствия: true — был на паре, false — отсутствует (энка)", example = "true")
    private Boolean isPresent;
}