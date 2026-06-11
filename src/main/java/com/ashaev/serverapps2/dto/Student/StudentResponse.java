package com.ashaev.serverapps2.dto.Student;

import com.ashaev.serverapps2.dto.Group.GroupResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о студенте")
public class StudentResponse {

    @Schema(description = "Уникальный идентификатор студента", example = "1")
    private Long id;

    @Schema(description = "Полное имя студента", example = "Иконников Богдан Сергеевич")
    private String fullName;

    @Schema(description = "Информация об учебной группе студента")
    private GroupResponse group;
}