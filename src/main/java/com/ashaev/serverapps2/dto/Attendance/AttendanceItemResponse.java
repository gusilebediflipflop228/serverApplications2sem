package com.ashaev.serverapps2.dto.Attendance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceItemResponse {
    private Long attendanceId;
    private Long studentId;
    private String studentFullName;
    private Boolean isPresent;
}