package com.ashaev.serverapps2.dto.Lesson;

import com.ashaev.serverapps2.dto.Attendance.AttendanceItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Полная информация о занятии")
public class LessonResponse {

    @Schema(description = "ID занятия", example = "1")
    private Long id;

    @Schema(description = "ID дисциплины", example = "1")
    private Long disciplineId;

    @Schema(description = "Название дисциплины", example = "Разработка серверных приложений")
    private String disciplineName;

    @Schema(description = "ID группы", example = "1")
    private Long groupId;

    @Schema(description = "Название группы", example = "ИП-211")
    private String groupName;

    @Schema(description = "ID преподавателя", example = "1")
    private Long teacherId;

    @Schema(description = "ФИО преподавателя", example = "Иванов Иван Иванович")
    private String teacherFullName;

    @Schema(description = "Дата проведения", example = "2026-05-25")
    private LocalDate classDate;

    @Schema(description = "Номер пары", example = "2")
    private Integer classNumber;

    @Schema(description = "Список отметок о посещаемости")
    private List<AttendanceItemResponse> attendances;
}