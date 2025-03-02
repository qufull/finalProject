package com.example.demo.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
