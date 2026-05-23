package com.ashaev.serverapps2.dto.Teacher;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherResponse {
    private Long id;
    private String fullName;
    private Long userId; // Возвращаем ID юзера, если он привязан
}