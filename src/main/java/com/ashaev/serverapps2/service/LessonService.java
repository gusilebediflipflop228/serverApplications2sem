package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Attendance.AttendanceItemResponse;
import com.ashaev.serverapps2.dto.Attendance.AttendanceUpdateRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonRequest;
import com.ashaev.serverapps2.dto.Lesson.LessonResponse;
import com.ashaev.serverapps2.entity.*;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND, id));

        Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, request.getDisciplineId()));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, request.getGroupId()));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, request.getTeacherId()));

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
            throw new AppException(ErrorCode.LESSON_NOT_FOUND, id);
        }
        lessonRepository.deleteById(id);
    }

    public List<LessonResponse> getLessonsPagedWithCheck(LocalDate start, LocalDate end, Long groupId, Long teacherId, int page, int size) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, currentUser.getUsername()));
            groupId = student.getGroup().getId();
        }

        return getLessonsPaged(start, end, groupId, teacherId, page, size);
    }

    @Transactional
    public LessonResponse createLesson(LessonRequest request) {
        Discipline discipline = disciplineRepository.findById(request.getDisciplineId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, request.getDisciplineId()));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, request.getGroupId()));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, request.getTeacherId()));

        Lesson lesson = Lesson.builder()
                .discipline(discipline)
                .group(group)
                .teacher(teacher)
                .classDate(request.getClassDate())
                .classNumber(request.getClassNumber())
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);

        List<Attendance> attendances = studentRepository.findByGroupId(group.getId()).stream()
                .map(student -> Attendance.builder()
                        .lesson(savedLesson)
                        .student(student)
                        .isPresent(false)
                        .build())
                .toList();

        attendanceRepository.saveAll(attendances);
        savedLesson.setAttendances(attendances);

        return mapToResponse(savedLesson, false);
    }

    public LessonResponse getLessonByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND, id));

        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, currentUser.getUsername()));

            if (!lesson.getGroup().getId().equals(student.getGroup().getId())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
        return mapToResponse(lesson, true);
    }

    public List<LessonResponse> getLessonsPaged(LocalDate start, LocalDate end, Long groupId, Long teacherId, int page, int size) {
        return lessonRepository.findLessonsForPeriod(start, end, groupId, teacherId, PageRequest.of(page, size))
                .map(lesson -> mapToResponse(lesson, false))
                .getContent();
    }

    @Transactional
    public void updateAttendance(Long lessonId, List<AttendanceUpdateRequest> requests) {
        for (AttendanceUpdateRequest req : requests) {
            Attendance attendance = attendanceRepository.findByLessonIdAndStudentId(lessonId, req.getStudentId())
                    .orElseThrow(() -> new AppException(ErrorCode.ATTENDANCE_NOT_FOUND));
            attendance.setIsPresent(req.getIsPresent());
        }
    }

    private LessonResponse mapToResponse(Lesson lesson, boolean includeAttendance) {
        List<AttendanceItemResponse> attendanceList = null;
        if (includeAttendance && lesson.getAttendances() != null) {
            attendanceList = lesson.getAttendances().stream()
                    .map(a -> new AttendanceItemResponse(a.getId(), a.getStudent().getId(), a.getStudent().getFullName(), a.getIsPresent()))
                    .toList();
        }

        return new LessonResponse(
                lesson.getId(), lesson.getDiscipline().getId(), lesson.getDiscipline().getName(),
                lesson.getGroup().getId(), lesson.getGroup().getName(),
                lesson.getTeacher().getId(), lesson.getTeacher().getFullName(),
                lesson.getClassDate(), lesson.getClassNumber(), attendanceList
        );
    }
}