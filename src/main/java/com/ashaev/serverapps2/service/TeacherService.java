package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Auth.RegisterTeacherRequest;
import com.ashaev.serverapps2.dto.Teacher.TeacherRequest;
import com.ashaev.serverapps2.dto.Teacher.TeacherResponse;
import com.ashaev.serverapps2.entity.Role;
import com.ashaev.serverapps2.entity.Teacher;
import com.ashaev.serverapps2.entity.User;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.TeacherRepository;
import com.ashaev.serverapps2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;


    public List<TeacherResponse> getAllTeachersPaged(int page, int size) {
        return teacherRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse)
                .toList();
    }

    public TeacherResponse getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, id));
    }

    @Transactional
    public TeacherResponse createTeacher(RegisterTeacherRequest request) {
        if (teacherRepository.existsByFullName(request.fullName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Преподаватель '" + request.fullName() + "' уже существует");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.TEACHER)
                .build();

        user = userRepository.saveAndFlush(user);
        Teacher teacher = new Teacher();
        teacher.setFullName(request.fullName());
        teacher.setUser(user);
        Teacher savedTeacher = teacherRepository.save(teacher);

        return mapToResponse(savedTeacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, id));

        if (!teacher.getFullName().equals(request.getFullName()) && teacherRepository.existsByFullName(request.getFullName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Преподаватель '" + request.getFullName() + "' уже существует");
        }

        teacher.setFullName(request.getFullName());
        return mapToResponse(teacher);
    }


    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new AppException(ErrorCode.TEACHER_NOT_FOUND, id);
        }

        try {
            teacherRepository.deleteById(id);
            teacherRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DEPENDENCY_VIOLATION,
                    "Невозможно удалить, так как есть зависимые данные: учитель закреплен за расписанием");
        }
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        Long userId = (teacher.getUser() != null) ? teacher.getUser().getId() : null;
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), userId);
    }
}