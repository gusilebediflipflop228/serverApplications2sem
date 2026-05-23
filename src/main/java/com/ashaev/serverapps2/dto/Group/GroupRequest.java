package com.ashaev.serverapps2.dto.Group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Форма для создания учебной группы")
public class GroupRequest {
    @NotBlank(message = "Название группы не должно быть пустым")
    @Schema(description = "Название академической группы", example = "ИП-211")
    private String name;
}