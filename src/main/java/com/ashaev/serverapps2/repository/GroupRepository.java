package com.ashaev.serverapps2.repository;

import com.ashaev.serverapps2.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);
    Optional<Group> findByName(String name);
}