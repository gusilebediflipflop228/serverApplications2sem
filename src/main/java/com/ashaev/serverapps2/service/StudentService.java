package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.dto.Student.StudentRequest;
import com.ashaev.serverapps2.dto.Student.StudentResponse;
import com.ashaev.serverapps2.entity.*;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.GroupRepository;
import com.ashaev.serverapps2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public List<StudentResponse> getStudentsByGroupId(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, groupId);
        }
        return studentRepository.findByGroupId(groupId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StudentResponse> getStudentsByGroupIdWithCheck(Long groupId) {
        checkStudentAccessGroup(groupId);
        return getStudentsByGroupId(groupId);
    }

    public StudentResponse getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, id));
    }

    public StudentResponse getStudentByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, currentUser.getUsername()));

            if (!student.getId().equals(id)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
        return getStudentById(id);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, request.getGroupId()));

        if (studentRepository.existsByFullName(request.getFullName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Студент '" + request.getFullName() + "' уже существует");
        }

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setGroup(group);

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, id));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, request.getGroupId()));

        if (!student.getFullName().equals(request.getFullName()) && studentRepository.existsByFullName(request.getFullName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Студент '" + request.getFullName() + "' уже существует");
        }

        student.setFullName(request.getFullName());
        student.setGroup(group);

        return mapToResponse(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, id);
        }
        studentRepository.deleteById(id);
    }

    private StudentResponse mapToResponse(Student student) {
        GroupResponse groupResponse = new GroupResponse(student.getGroup().getId(), student.getGroup().getName());
        return new StudentResponse(student.getId(), student.getFullName(), groupResponse);
    }

    private void checkStudentAccessGroup(Long groupId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, currentUser.getUsername()));

            if (!student.getGroup().getId().equals(groupId)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
    }
}