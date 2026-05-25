package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.repository.StudentRepository;
import com.ashaev.serverapps2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь не найден");
        }

        studentRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}