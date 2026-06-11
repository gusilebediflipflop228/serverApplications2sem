package com.ashaev.serverapps2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "disciplines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discipline that)) return false;
        return id != null && id.equals(that.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}