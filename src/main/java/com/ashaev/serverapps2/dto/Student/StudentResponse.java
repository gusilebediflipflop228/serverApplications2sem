package com.ashaev.serverapps2.dto.Student;

import com.ashaev.serverapps2.dto.Group.GroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String fullName;
    private GroupResponse group;
}