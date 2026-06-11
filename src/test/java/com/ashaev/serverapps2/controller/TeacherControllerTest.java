package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Teacher.TeacherRequest;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.SecurityConfig;
import com.ashaev.serverapps2.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@Import(SecurityConfig.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeacherService teacherService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private com.ashaev.serverapps2.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTeacher_WhenUsedInClasses_ReturnsConflict() throws Exception {
        Long teacherId = 1L;

        doThrow(new AppException(ErrorCode.DEPENDENCY_VIOLATION, "учитель закреплен за расписанием"))
                .when(teacherService).deleteTeacher(teacherId);

        mockMvc.perform(delete("/api/v1/teachers/{id}", teacherId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.DEPENDENCY_VIOLATION.getCode()))
                .andExpect(jsonPath("$.errorMessage").value("Невозможно удалить, так как есть зависимые данные: учитель закреплен за расписанием"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getAllTeachers_ReturnsList() throws Exception {
        when(teacherService.getAllTeachersPaged(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/teachers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTeacher_ValidRequest_ReturnsOk() throws Exception {
        TeacherRequest request = new TeacherRequest("Иванов Иван Иванович");

        mockMvc.perform(put("/api/v1/teachers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}