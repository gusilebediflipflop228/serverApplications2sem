package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.StudentRepository;
import com.ashaev.serverapps2.repository.TeacherRepository;
import com.ashaev.serverapps2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, id);
        }

        try {
            studentRepository.deleteByUserId(id);
            teacherRepository.deleteByUserId(id);
            userRepository.deleteById(id);

        } catch (Exception e) {
            throw new AppException(ErrorCode.DEPENDENCY_VIOLATION, "Невозможно удалить пользователя, так как с ним связаны другие данные");
        }
    }
}