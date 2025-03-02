package com.example.demo.service;

import com.example.demo.enums.UserRoles;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(UserRoles role){
        return roleRepository.findByRole(role).orElseThrow(() -> new RuntimeException("Role not found"));
    }


}
