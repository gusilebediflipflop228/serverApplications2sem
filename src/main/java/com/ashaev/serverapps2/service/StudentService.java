package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.dto.Student.StudentRequest;
import com.ashaev.serverapps2.dto.Student.StudentResponse;
import com.ashaev.serverapps2.entity.Group;
import com.ashaev.serverapps2.entity.Role;
import com.ashaev.serverapps2.entity.Student;
import com.ashaev.serverapps2.entity.User;
import com.ashaev.serverapps2.repository.GroupRepository;
import com.ashaev.serverapps2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException; // Импорт обязателен
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public List<StudentResponse> getStudentsByGroupId(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new RuntimeException("Группа с ID " + groupId + " не найдена");
        }
        return studentRepository.findByGroupId(groupId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsByGroupIdWithCheck(Long groupId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AccessDeniedException("Вы не числитесь в системе как студент"));

            if (!student.getGroup().getId().equals(groupId)) {
                throw new AccessDeniedException("Доступ запрещен. Вы можете просматривать только студентов своей группы.");
            }
        }

        return getStudentsByGroupId(groupId);
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с ID " + id + " не найден"));
        return mapToResponse(student);
    }

    public StudentResponse getStudentByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AccessDeniedException("Вы не числитесь в системе как student"));

            if (!student.getId().equals(id)) {
                throw new AccessDeniedException("Доступ запрещен. Студент может просматривать только свою личную информацию.");
            }
        }

        return getStudentById(id);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Нельзя создать студента: группа с ID " + request.getGroupId() + " не существует"));

        if (studentRepository.existsByFullName(request.getFullName())) {
            throw new RuntimeException("Студент с ФИО '" + request.getFullName() + "' уже существует");
        }

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setGroup(group);

        Student savedStudent = studentRepository.save(student);
        return mapToResponse(savedStudent);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с ID " + id + " не найден"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Группа с ID " + request.getGroupId() + " не существует"));

        if (!student.getFullName().equals(request.getFullName()) && studentRepository.existsByFullName(request.getFullName())) {
            throw new RuntimeException("Студент с ФИО '" + request.getFullName() + "' уже существует");
        }

        student.setFullName(request.getFullName());
        student.setGroup(group);

        return mapToResponse(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Студент с ID " + id + " не найден");
        }
        studentRepository.deleteById(id);
    }

    private StudentResponse mapToResponse(Student student) {
        GroupResponse groupResponse = new GroupResponse(student.getGroup().getId(), student.getGroup().getName());
        return new StudentResponse(student.getId(), student.getFullName(), groupResponse);
    }
}