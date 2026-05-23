package com.ashaev.serverapps2.dto.Teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Форма для добавления преподавателя")
public class TeacherRequest {
    @NotBlank(message = "ФИО преподавателя не должно быть пустым")
    @Schema(description = "Полное имя преподавателя", example = "Ашаев Иван Игоревич")
    private String fullName;
}