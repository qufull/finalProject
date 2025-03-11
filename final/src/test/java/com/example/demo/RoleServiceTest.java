package com.example.demo;

import com.example.demo.enums.UserRoles;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private UserRoles userRole;

    @BeforeEach
    void setUp() {
        userRole = UserRoles.USER;
        role = new Role();
        role.setId(1L);
        role.setRole(userRole);
    }

    @Test
    void testFindByName_Success() {
        when(roleRepository.findByRole(any(UserRoles.class))).thenReturn(Optional.of(role));

        Role result = roleService.findByName(userRole);

        assertNotNull(result);
        assertEquals(userRole, result.getRole());
        verify(roleRepository, times(1)).findByRole(any(UserRoles.class));
    }

    @Test
    void testFindByName_RoleNotFound() {
        when(roleRepository.findByRole(any(UserRoles.class))).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> {
            roleService.findByName(userRole);
        });

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findByRole(any(UserRoles.class));
    }
}
