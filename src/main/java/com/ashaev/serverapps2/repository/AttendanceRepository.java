package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Поиск конкретной отметки студента на конкретном занятии
    Optional<Attendance> findByLessonIdAndStudentId(Long lessonId, Long studentId);
}