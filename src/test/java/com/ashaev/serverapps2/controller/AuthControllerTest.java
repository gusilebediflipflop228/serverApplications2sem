package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Auth.*;
import com.ashaev.serverapps2.entity.Role;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.JwtService;
import com.ashaev.serverapps2.security.SecurityConfig;
import com.ashaev.serverapps2.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void login_ValidRequest_ReturnsOk() throws Exception {
        LoginRequest request = new LoginRequest("user", "password");
        when(authService.login(any())).thenReturn(new AuthResponse("access-token", "refresh-token", Role.STUDENT));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerTeacher_AsAdmin_ReturnsOk() throws Exception {
        RegisterTeacherRequest request = new RegisterTeacherRequest("teacher", "pass", "Name");
        when(authService.registerTeacher(any())).thenReturn(new AuthResponse("at", "rt", Role.TEACHER));

        mockMvc.perform(post("/api/v1/auth/register/teacher")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void registerTeacher_AsStudent_ReturnsForbidden() throws Exception {
        RegisterTeacherRequest request = new RegisterTeacherRequest("teacher", "pass", "Name");

        mockMvc.perform(post("/api/v1/auth/register/teacher")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}