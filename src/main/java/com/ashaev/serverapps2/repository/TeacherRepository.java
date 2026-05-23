package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByFullName(String fullName);
}