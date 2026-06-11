package com.ashaev.serverapps2.dto.Student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Форма для добавления нового студента")
public class StudentRequest {

    @NotBlank(message = "ФИО не должно быть пустым")
    @Size(min = 5, max = 255, message = "ФИО должно быть от 5 до 255 символов")
    @Schema(description = "Полное имя студента", example = "Иконников Богдан Сергеевич", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @NotNull(message = "ID группы обязателен")
    @Schema(description = "Идентификатор группы, к которой привязан студент", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long groupId;
}