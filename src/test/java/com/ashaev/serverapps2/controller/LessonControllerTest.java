package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Lesson.LessonRequest;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.SecurityConfig;
import com.ashaev.serverapps2.service.LessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@Import(SecurityConfig.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private com.ashaev.serverapps2.security.JwtService jwtService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createLesson_ValidRequest_ReturnsOk() throws Exception {
        LessonRequest request = new LessonRequest();
        request.setDisciplineId(1L);
        request.setGroupId(1L);
        request.setTeacherId(1L);
        request.setClassDate(LocalDate.now());
        request.setClassNumber(1);

        mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getLessons_WithoutParams_ReturnsOk() throws Exception {
        when(lessonService.getLessonsPagedWithCheck(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getLessons_WithDateParams_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/lessons")
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-06-30"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAttendance_ValidRequest_ReturnsOk() throws Exception {
        mockMvc.perform(put("/api/v1/lessons/{id}/attendance", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void updateAttendance_AsStudent_ReturnsForbidden() throws Exception {
        mockMvc.perform(put("/api/v1/lessons/{id}/attendance", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isForbidden());
    }
}