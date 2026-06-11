package com.ashaev.serverapps2.dto.Attendance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о посещаемости студента")
public class AttendanceItemResponse {

    @Schema(description = "ID записи о посещаемости", example = "1")
    private Long attendanceId;

    @Schema(description = "ID студента", example = "105")
    private Long studentId;

    @Schema(description = "ФИО студента", example = "Иванов Иван Иванович")
    private String studentFullName;

    @Schema(description = "Статус посещаемости (присутствует/отсутствует)", example = "true")
    private Boolean isPresent;
}