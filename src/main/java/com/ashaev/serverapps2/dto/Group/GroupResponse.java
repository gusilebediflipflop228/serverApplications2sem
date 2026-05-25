package com.ashaev.serverapps2.dto.Group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными учебной группы")
public class GroupResponse {
    @Schema(description = "Уникальный идентификатор группы", example = "1")
    private Long id;
    private String name;
}