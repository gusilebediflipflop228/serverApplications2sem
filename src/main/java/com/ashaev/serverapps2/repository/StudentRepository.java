package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByGroupId(Long groupId);

    boolean existsByFullName(String fullName);
}