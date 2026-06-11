package com.ashaev.serverapps2.dto.Group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Форма для создания учебной группы")
public class GroupRequest {

    @NotBlank(message = "Название группы не должно быть пустым")
    @Size(min = 2, max = 20, message = "Название группы должно быть от 2 до 20 символов")
    @Schema(description = "Название академической группы", example = "ИП-211", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}