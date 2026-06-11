package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Discipline.DisciplineRequest;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.SecurityConfig;
import com.ashaev.serverapps2.service.DisciplineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisciplineController.class)
@Import(SecurityConfig.class)
public class DisciplineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisciplineService disciplineService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private com.ashaev.serverapps2.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDiscipline_WhenUsedInClasses_ReturnsConflict() throws Exception {
        Long disciplineId = 1L;
        doThrow(new AppException(ErrorCode.DEPENDENCY_VIOLATION, "дисциплина используется в расписании"))
                .when(disciplineService).deleteDiscipline(disciplineId);

        mockMvc.perform(delete("/api/v1/disciplines/{id}", disciplineId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.DEPENDENCY_VIOLATION.getCode()))
                .andExpect(jsonPath("$.errorMessage").value("Невозможно удалить, так как есть зависимые данные: дисциплина используется в расписании"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDiscipline_ValidRequest_ReturnsOk() throws Exception {
        DisciplineRequest request = new DisciplineRequest("Математика");

        mockMvc.perform(post("/api/v1/disciplines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void createDiscipline_AsStudent_ReturnsForbidden() throws Exception {
        DisciplineRequest request = new DisciplineRequest("Математика");

        mockMvc.perform(post("/api/v1/disciplines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}