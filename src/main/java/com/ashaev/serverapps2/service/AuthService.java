package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Auth.*;
import com.ashaev.serverapps2.entity.*;
import com.ashaev.serverapps2.repository.GroupRepository;
import com.ashaev.serverapps2.repository.StudentRepository;
import com.ashaev.serverapps2.repository.TeacherRepository;
import com.ashaev.serverapps2.repository.UserRepository;
import com.ashaev.serverapps2.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var user = userRepository.findByUsername(request.username()).orElseThrow();
        var jwt = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(jwt, refreshToken, user.getRole());
    }

    @Transactional
    public AuthResponse registerStudent(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        Group group = groupRepository.findByName(request.groupCode())
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STUDENT)
                .build();
        userRepository.save(user);

        studentRepository.save(Student.builder().user(user).group(group).fullName(request.fullName()).build());

        return new AuthResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user), user.getRole());
    }

    @Transactional
    public AuthResponse registerTeacher(RegisterTeacherRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.TEACHER)
                .build();
        userRepository.save(user);

        teacherRepository.save(Teacher.builder().user(user).fullName(request.fullName()).build());

        return new AuthResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user), user.getRole());
    }

    @Transactional
    public AuthResponse registerAdmin(RegisterAdminRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        return new AuthResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user), user.getRole());
    }
}