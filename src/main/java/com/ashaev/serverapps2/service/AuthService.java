package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Auth.*;
import com.ashaev.serverapps2.entity.*;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.*;
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

        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, request.username()));

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse registerStudent(RegisterRequest request) {
        checkUserExists(request.username());

        Group group = groupRepository.findByName(request.groupCode())
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, request.groupCode()));

        var user = createUser(request.username(), request.password(), Role.STUDENT);

        studentRepository.save(Student.builder()
                .user(user)
                .group(group)
                .fullName(request.fullName())
                .build());

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse registerTeacher(RegisterTeacherRequest request) {
        checkUserExists(request.username());

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.TEACHER)
                .build();

        // ПРЯМО ТУТ ДЕЛАЕМ СОХРАНЕНИЕ
        user = userRepository.save(user);
        userRepository.flush(); // ОБЯЗАТЕЛЬНО СБРОС

        // ПРОВЕРКА
        System.err.println("DEBUG: User saved with ID: " + user.getId());

        if (user.getId() == null) {
            throw new RuntimeException("ОШИБКА: Пользователь не сохранился, ID пустой!");
        }

        Teacher teacher = new Teacher();
        teacher.setFullName(request.fullName());
        teacher.setUser(user);

        teacherRepository.save(teacher);

        return createAuthResponse(user);
    }


    @Transactional
    public AuthResponse registerAdmin(RegisterAdminRequest request) {
        checkUserExists(request.username());
        var user = createUser(request.username(), request.password(), Role.ADMIN);
        return createAuthResponse(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.refreshToken());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, username));

        if (!jwtService.isTokenValid(request.refreshToken(), username)) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return createAuthResponse(user);
    }

    private void checkUserExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS, username);
        }
    }

    private User createUser(String username, String password, Role role) {
        return userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build());
    }

    private AuthResponse createAuthResponse(User user) {
        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                user.getRole()
        );
    }
}