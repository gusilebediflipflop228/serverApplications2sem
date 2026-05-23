package com.ashaev.serverapps2.dto.Discipline;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Форма для создания новой учебной дисциплины")
public class DisciplineRequest {
    @NotBlank(message = "Название дисциплины не должно быть пустым")
    @Schema(description = "Название предмета", example = "Разработка серверных приложений")
    private String name;
}