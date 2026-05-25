package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Group.GroupRequest;
import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.entity.Group;
import com.ashaev.serverapps2.entity.Role;
import com.ashaev.serverapps2.entity.User;
import com.ashaev.serverapps2.repository.GroupRepository;
import com.ashaev.serverapps2.repository.StudentRepository; // Импортируем твой репозиторий студентов
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException; // Важно для правильной 403 ошибки
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(group -> new GroupResponse(group.getId(), group.getName()))
                .collect(Collectors.toList());
    }

    public GroupResponse getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа с ID " + id + " не найдена"));
        return new GroupResponse(group.getId(), group.getName());
    }

    public GroupResponse getGroupByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.STUDENT) {
            var student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AccessDeniedException("Вы не числитесь в системе как студент"));

            if (!student.getGroup().getId().equals(id)) {
                throw new AccessDeniedException("Доступ запрещен. Студент может просматривать только свою группу.");
            }
        }
        return getGroupById(id);
    }

    @Transactional
    public GroupResponse createGroup(GroupRequest request) {
        if (groupRepository.existsByName(request.getName())) {
            throw new RuntimeException("Группа с названием '" + request.getName() + "' уже существует");
        }
        Group group = new Group();
        group.setName(request.getName());
        Group savedGroup = groupRepository.save(group);
        return new GroupResponse(savedGroup.getId(), savedGroup.getName());
    }

    @Transactional
    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа с ID " + id + " не найдена"));

        if (!group.getName().equals(request.getName()) && groupRepository.existsByName(request.getName())) {
            throw new RuntimeException("Группа с названием '" + request.getName() + "' уже существует");
        }

        group.setName(request.getName());
        return new GroupResponse(group.getId(), group.getName());
    }

    @Transactional
    public void deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Группа с ID " + id + " не найдена");
        }
        groupRepository.deleteById(id);
    }
}