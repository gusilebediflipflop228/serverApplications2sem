package com.ashaev.serverapps2.dto.Discipline;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о дисциплине")
public class DisciplineResponse {

    @Schema(description = "Уникальный идентификатор дисциплины", example = "1")
    private Long id;

    @Schema(description = "Название дисциплины", example = "Разработка серверных приложений")
    private String name;
}