package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Student;
import com.ashaev.serverapps2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    void deleteByUserId(Long userId);

    Optional<Student> findByUser(User user);

    List<Student> findByGroupId(Long groupId);

    boolean existsByFullName(String fullName);
}