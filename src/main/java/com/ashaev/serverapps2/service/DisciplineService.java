package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Discipline.DisciplineRequest;
import com.ashaev.serverapps2.dto.Discipline.DisciplineResponse;
import com.ashaev.serverapps2.entity.Discipline;
import com.ashaev.serverapps2.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<DisciplineResponse> getAllDisciplines() {
        return disciplineRepository.findAll().stream()
                .map(d -> new DisciplineResponse(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }

    public DisciplineResponse getDisciplineById(Long id) {
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Дисциплина с ID " + id + " не найдена"));
        return new DisciplineResponse(discipline.getId(), discipline.getName());
    }

    @Transactional
    public DisciplineResponse createDiscipline(DisciplineRequest request) {
        if (disciplineRepository.existsByName(request.getName())) {
            throw new RuntimeException("Дисциплина '" + request.getName() + "' уже существует");
        }
        Discipline discipline = new Discipline();
        discipline.setName(request.getName());
        Discipline saved = disciplineRepository.save(discipline);
        return new DisciplineResponse(saved.getId(), saved.getName());
    }

    @Transactional
    public DisciplineResponse updateDiscipline(Long id, DisciplineRequest request) {
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Дисциплина с ID " + id + " не найдена"));

        if (!discipline.getName().equals(request.getName()) && disciplineRepository.existsByName(request.getName())) {
            throw new RuntimeException("Дисциплина '" + request.getName() + "' уже существует");
        }

        discipline.setName(request.getName());
        return new DisciplineResponse(discipline.getId(), discipline.getName());
    }

    @Transactional
    public void deleteDiscipline(Long id) {
        if (!disciplineRepository.existsById(id)) {
            throw new RuntimeException("Дисциплина с ID " + id + " не найдена");
        }
        disciplineRepository.deleteById(id);
    }
}