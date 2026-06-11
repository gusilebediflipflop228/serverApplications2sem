package com.ashaev.serverapps2.controller;

import com.ashaev.serverapps2.dto.Group.GroupRequest;
import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.JwtService;
import com.ashaev.serverapps2.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGroup_ValidRequest_ReturnsOk() throws Exception {
        GroupRequest request = new GroupRequest("Группа 1");
        GroupResponse response = new GroupResponse(1L, "Группа 1");

        when(groupService.createGroup(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/groups")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Группа 1"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAllGroups_AsTeacher_ReturnsOk() throws Exception {
        when(groupService.getAllGroups()).thenReturn(List.of(new GroupResponse(1L, "Группа 1")));

        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroup_ValidId_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/groups/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Учебная группа успешно удалена"));
    }
}