package com.ashaev.serverapps2.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT,
    TEACHER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}