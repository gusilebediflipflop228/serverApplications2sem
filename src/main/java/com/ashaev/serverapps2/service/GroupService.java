package com.ashaev.serverapps2.service;

import com.ashaev.serverapps2.dto.Group.GroupRequest;
import com.ashaev.serverapps2.dto.Group.GroupResponse;
import com.ashaev.serverapps2.entity.Group;
import com.ashaev.serverapps2.entity.Role;
import com.ashaev.serverapps2.entity.User;
import com.ashaev.serverapps2.exception.AppException;
import com.ashaev.serverapps2.exception.ErrorCode;
import com.ashaev.serverapps2.repository.GroupRepository;
import com.ashaev.serverapps2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public GroupResponse getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, id));
    }

    public GroupResponse getGroupByIdWithCheck(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.STUDENT) {
            var student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, currentUser.getUsername()));

            if (!student.getGroup().getId().equals(id)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
        return getGroupById(id);
    }

    @Transactional
    public GroupResponse createGroup(GroupRequest request) {
        if (groupRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Группа с названием '" + request.getName() + "' уже существует");
        }
        Group group = new Group();
        group.setName(request.getName());
        return mapToResponse(groupRepository.save(group));
    }

    @Transactional
    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, id));

        if (!group.getName().equals(request.getName()) && groupRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Группа с названием '" + request.getName() + "' уже существует");
        }

        group.setName(request.getName());
        return mapToResponse(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_FOUND, id));

        if (studentRepository.countByGroupId(id) > 0) {
            throw new AppException(ErrorCode.DEPENDENCY_VIOLATION, "Группу невозможно удалить, так как в ней есть студенты");
        }

        groupRepository.delete(group);
    }

    private GroupResponse mapToResponse(Group group) {
        return new GroupResponse(group.getId(), group.getName());
    }
}