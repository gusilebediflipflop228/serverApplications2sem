package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // Поиск занятий за период постранично с опциональными фильтрами по группе и преподавателю
    @Query("SELECT l FROM Lesson l WHERE l.classDate BETWEEN :startDate AND :endDate " +
            "AND (:groupId IS NULL OR l.group.id = :groupId) " +
            "AND (:teacherId IS NULL OR l.teacher.id = :teacherId)")
    Page<Lesson> findLessonsForPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("groupId") Long groupId,
            @Param("teacherId") Long teacherId,
            Pageable pageable
    );
}