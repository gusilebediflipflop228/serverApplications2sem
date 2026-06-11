package com.ashaev.serverapps2.dto.Teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о преподавателе")
public class TeacherResponse {

    @Schema(description = "Уникальный идентификатор преподавателя", example = "1")
    private Long id;

    @Schema(description = "Полное имя преподавателя", example = "Ашаев Игорь Викторович")
    private String fullName;

    @Schema(description = "Идентификатор связанного аккаунта пользователя", example = "10")
    private Long userId;
}