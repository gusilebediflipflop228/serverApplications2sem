package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    boolean existsByName(String name);
}