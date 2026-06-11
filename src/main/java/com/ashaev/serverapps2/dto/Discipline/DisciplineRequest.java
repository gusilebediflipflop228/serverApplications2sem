package com.ashaev.serverapps2.dto.Discipline;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Форма для создания новой учебной дисциплины")
public class DisciplineRequest {

    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
    @Schema(description = "Название предмета", example = "Разработка серверных приложений", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}