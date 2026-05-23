package com.ashaev.serverapps2.dto.Lesson;

import com.ashaev.serverapps2.dto.Attendance.AttendanceItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class LessonResponse {
    private Long id;
    private Long disciplineId;
    private String disciplineName;
    private Long groupId;
    private String groupName;
    private Long teacherId;
    private String teacherFullName;
    private LocalDate classDate;
    private Integer classNumber;
    private List<AttendanceItemResponse> attendances; // Будет заполнено только при GET по ID
}