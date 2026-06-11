package com.ashaev.serverapps2.dto.Attendance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для отметки присутствия конкретного студента")
public class AttendanceUpdateRequest {

    @NotNull(message = "ID студента обязателен")
    @Positive(message = "ID студента должен быть положительным числом")
    @Schema(description = "Уникальный идентификатор студента", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long studentId;

    @NotNull(message = "Статус присутствия обязателен")
    @Schema(description = "Флаг присутствия: true — был на паре, false — отсутствует (энка)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isPresent;
}