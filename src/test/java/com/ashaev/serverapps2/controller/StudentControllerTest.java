package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Student.StudentRequest;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.SecurityConfig;
import com.ashaev.serverapps2.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private com.ashaev.serverapps2.security.JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStudent_ValidRequest_ReturnsOk() throws Exception {
        StudentRequest request = new StudentRequest("Иван Иванов", 1L);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getStudentsByGroupId_AsStudent_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/students/group/{groupId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent_WhenAdmin_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void deleteStudent_AsStudent_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentById_NotFound_ReturnsNotFound() throws Exception {
        doThrow(new AppException(ErrorCode.STUDENT_NOT_FOUND, 99L))
                .when(studentService).getStudentByIdWithCheck(99L);

        mockMvc.perform(get("/api/v1/students/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}