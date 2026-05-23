package com.ashaev.serverapps2.dto.Student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Форма для добавления нового студента")
public class StudentRequest {
    @NotBlank(message = "ФИО не должно быть пустым")
    @Schema(description = "Полное имя студента", example = "Иконников Богдан Сергеевич")
    private String fullName;

    @NotNull(message = "ID группы обязателен")
    @Schema(description = "Идентификатор группы, к которой привязан студент", example = "1")
    private Long groupId;
}