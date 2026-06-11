package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Teacher;
import com.ashaev.serverapps2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByFullName(String fullName);
    void deleteByUserId(Long userId);
    boolean existsByUserId(Long userId);
}