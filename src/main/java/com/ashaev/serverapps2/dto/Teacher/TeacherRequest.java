package com.ashaev.serverapps2.dto.Teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Форма для добавления преподавателя")
public class TeacherRequest {
    @NotBlank(message = "ФИО преподавателя не должно быть пустым")
    @Size(min = 5, max = 255, message = "ФИО должно быть от 5 до 255 символов")
    @Schema(description = "Полное имя преподавателя", example = "Ашаев Игорь Викторович", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
}