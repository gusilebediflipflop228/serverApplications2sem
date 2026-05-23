package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Teacher.TeacherRequest;
import com.ashaev.serverapps2.dto.Teacher.TeacherResponse;
import com.ashaev.serverapps2.entity.Teacher;
import com.ashaev.serverapps2.repository.TeacherRepository;
// import com.ashaev.serverapps2.repository.UserRepository; // Раскомментируй, если есть
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    // private final UserRepository userRepository; // Раскомментируй, если есть

    public List<TeacherResponse> getAllTeachersPaged(int page, int size) {
        Page<Teacher> teacherPage = teacherRepository.findAll(PageRequest.of(page, size));
        return teacherPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Преподаватель с ID " + id + " не найден"));
        return mapToResponse(teacher);
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        if (teacherRepository.existsByFullName(request.getFullName())) {
            throw new RuntimeException("Преподаватель с ФИО '" + request.getFullName() + "' уже существует");
        }

        Teacher teacher = new Teacher();
        teacher.setFullName(request.getFullName());

        // Если передан userId, привязываем пользователя (раскомментируй, когда будет UserRepository)
        /*
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + request.getUserId() + " не найден"));
            teacher.setUser(user);
        }
        */

        Teacher saved = teacherRepository.save(teacher);
        return mapToResponse(saved);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Преподаватель с ID " + id + " не найден"));

        if (!teacher.getFullName().equals(request.getFullName()) && teacherRepository.existsByFullName(request.getFullName())) {
            throw new RuntimeException("Преподаватель с ФИО '" + request.getFullName() + "' уже существует");
        }

        teacher.setFullName(request.getFullName());

        /* Изменение юзера при обновлении (опционально)
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + request.getUserId() + " не найден"));
            teacher.setUser(user);
        } else {
            teacher.setUser(null);
        }
        */

        return mapToResponse(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Преподаватель с ID " + id + " не найден");
        }
        teacherRepository.deleteById(id);
    }

    // Вспомогательный метод маппинга
    private TeacherResponse mapToResponse(Teacher teacher) {
        Long userId = (teacher.getUser() != null) ? teacher.getUser().getId() : null;
        return new TeacherResponse(teacher.getId(), teacher.getFullName(), userId);
    }
}