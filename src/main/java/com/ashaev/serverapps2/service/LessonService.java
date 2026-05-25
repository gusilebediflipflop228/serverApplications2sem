package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Attendance.AttendanceItemResponse;
import com.ashaev.serverapps2.dto.Attendance.AttendanceUpdateRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonResponse;
import com.ashaev.serverapps2.entity.*;
import com.ashaev.serverapps2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException; // Импорт обязателен
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public LessonResponse createLesson(LessonRequest request) {
        Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(() -> new RuntimeException("Дисциплина с ID " + request.getDisciplineId() + " не найдена"));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Группа с ID " + request.getGroupId() + " не найдена"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Преподаватель с ID " + request.getTeacherId() + " не найден"));

        Lesson lesson = new Lesson();
        lesson.setDiscipline(discipline);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setClassDate(request.getClassDate());
        lesson.setClassNumber(request.getClassNumber());

        Lesson savedLesson = lessonRepository.save(lesson);

        List<Student> students = studentRepository.findByGroupId(group.getId());
        for (Student student : students) {
            Attendance attendance = new Attendance();
            attendance.setLesson(savedLesson);
            attendance.setStudent(student);
            attendance.setIsPresent(false);
            attendanceRepository.save(attendance);

            savedLesson.getAttendances().add(attendance);
        }

        return mapToResponse(savedLesson, false);
    }

    public LessonResponse getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Занятие с ID " + id + " не найдено"));
        return mapToResponse(lesson, true);
    }

    public LessonResponse getLessonByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Занятие с ID " + id + " не найдено"));

        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AccessDeniedException("Вы не числитесь в системе как студент"));

            if (!lesson.getGroup().getId().equals(student.getGroup().getId())) {
                throw new AccessDeniedException("Доступ запрещен. Вы можете просматривать ведомости только своей группы.");
            }
        }

        return mapToResponse(lesson, true);
    }

    public List<LessonResponse> getLessonsPaged(LocalDate start, LocalDate end, Long groupId, Long teacherId, int page, int size) {
        Page<Lesson> lessonPage = lessonRepository.findLessonsForPeriod(start, end, groupId, teacherId, PageRequest.of(page, size));
        return lessonPage.getContent().stream()
                .map(lesson -> mapToResponse(lesson, false))
                .collect(Collectors.toList());
    }

    public List<LessonResponse> getLessonsPagedWithCheck(LocalDate start, LocalDate end, Long groupId, Long teacherId, int page, int size) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AccessDeniedException("Вы не числитесь в системе как студент"));

            groupId = student.getGroup().getId();
        }

        return getLessonsPaged(start, end, groupId, teacherId, page, size);
    }

    @Transactional
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Занятие с ID " + id + " не найдено"));

        Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(() -> new RuntimeException("Дисциплина не найдена"));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));

        lesson.setDiscipline(discipline);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setClassDate(request.getClassDate());
        lesson.setClassNumber(request.getClassNumber());

        return mapToResponse(lesson, false);
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Занятие с ID " + id + " не найдено");
        }
        lessonRepository.deleteById(id);
    }

    @Transactional
    public void updateAttendance(Long lessonId, List<AttendanceUpdateRequest> requests) {
        for (AttendanceUpdateRequest req : requests) {
            Attendance attendance = attendanceRepository.findByLessonIdAndStudentId(lessonId, req.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Запись посещаемости для студента с ID " + req.getStudentId() + " на этом занятии не найдена"));
            attendance.setIsPresent(req.getIsPresent());
        }
    }

    private LessonResponse mapToResponse(Lesson lesson, boolean includeAttendance) {
        List<AttendanceItemResponse> attendanceList = null;

        if (includeAttendance && lesson.getAttendances() != null) {
            attendanceList = lesson.getAttendances().stream()
                    .map(a -> new AttendanceItemResponse(
                            a.getId(),
                            a.getStudent().getId(),
                            a.getStudent().getFullName(),
                            a.getIsPresent()
                    ))
                    .collect(Collectors.toList());
        }

        return new LessonResponse(
                lesson.getId(),
                lesson.getDiscipline().getId(),
                lesson.getDiscipline().getName(),
                lesson.getGroup().getId(),
                lesson.getGroup().getName(),
                lesson.getTeacher().getId(),
                lesson.getTeacher().getFullName(),
                lesson.getClassDate(),
                lesson.getClassNumber(),
                attendanceList
        );
    }
}