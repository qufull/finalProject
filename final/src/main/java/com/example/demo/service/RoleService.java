package com.example.demo.service;

import com.example.demo.model.enums.UserRoles;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(UserRoles role){
        return roleRepository.findByRole(role).orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }
}
