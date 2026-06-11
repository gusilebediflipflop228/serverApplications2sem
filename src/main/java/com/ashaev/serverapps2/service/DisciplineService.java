package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Discipline.DisciplineRequest;
import com.ashaev.serverapps2.dto.Discipline.DisciplineResponse;
import com.ashaev.serverapps2.entity.Discipline;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public List<DisciplineResponse> getAllDisciplines() {
        return disciplineRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DisciplineResponse getDisciplineById(Long id) {
        return disciplineRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, id));
    }

    @Transactional
    public DisciplineResponse createDiscipline(DisciplineRequest request) {
        if (disciplineRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Дисциплина '" + request.getName() + "' уже существует");
        }

        Discipline discipline = new Discipline();
        discipline.setName(request.getName());
        return mapToResponse(disciplineRepository.save(discipline));
    }

    @Transactional
    public DisciplineResponse updateDiscipline(Long id, DisciplineRequest request) {
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, id));
        if (!discipline.getName().equals(request.getName()) && disciplineRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Имя '" + request.getName() + "' уже занято");
        }

        discipline.setName(request.getName());
        return mapToResponse(discipline);
    }

    @Transactional
    public void deleteDiscipline(Long id) {
        if (!disciplineRepository.existsById(id)) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND, id);
        }

        try {
            disciplineRepository.deleteById(id);
            disciplineRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DEPENDENCY_VIOLATION, "дисциплина используется в расписании");
        }
    }

    private DisciplineResponse mapToResponse(Discipline d) {
        return new DisciplineResponse(d.getId(), d.getName());
    }
}