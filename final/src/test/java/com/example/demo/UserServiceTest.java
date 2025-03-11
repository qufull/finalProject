package com.example.demo;

import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.enums.UserRoles;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Credential;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UpdateUser updateUser;
    private ChangePasswordDto changePasswordDto;
    private UserStatusDto userStatusDto;
    private UserRoleDto userRoleDto;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        updateUser = new UpdateUser();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");

        changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setPassword("newPassword");

        userStatusDto = new UserStatusDto();
        userStatusDto.setStatus(UserStatus.ACTIVE);

        userRoleDto = new UserRoleDto();
        userRoleDto.setRole(Collections.singletonList(UserRoles.USER));

        role = new Role();
        role.setId(1L);
        role.setRole(UserRoles.USER);
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(new UserDto());

        UserDto result = userService.create(user);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        User changedUser = new User();
        changedUser.setId(1L);
        changedUser.setFirstName("John");
        changedUser.setLastName("Doe");

        when(userRepository.findUserWithCredentialsById(anyLong())).thenReturn(Optional.of(changedUser));
        when(userRepository.save(any(User.class))).thenReturn(changedUser);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(new UserDto());

        UserDto result = userService.updateUser(updateUser, user);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).updateUserFromUpdateUser(any(UpdateUser.class), any(User.class));
        verify(userMapper, times(1)).userToUserDto(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findUserWithCredentialsById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(updateUser, user);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_Success() {
        Credential credential = new Credential();
        credential.setPassword("oldPassword");
        user.setCredential(credential);

        when(credentialRepository.save(any(Credential.class))).thenReturn(credential);

        userService.changePassword(changePasswordDto, user);

        verify(credentialRepository, times(1)).save(any(Credential.class));
    }

    @Test
    void testGetUser_Success() {
        when(userMapper.userToUserDto(any(User.class))).thenReturn(new UserDto());

        UserDto result = userService.getUser(user);

        assertNotNull(result);
    }

    @Test
    void testGetAdminPanelUsers_Success() {
        when(userRepository.findUsersWithRolesById(anyLong())).thenReturn(Optional.of(Collections.singletonList(user)));
        when(userMapper.userToAdminPanelUserDtoList(anyList())).thenReturn(Collections.singletonList(new AdminPanelUserDto()));

        List<AdminPanelUserDto> result = userService.getAdminPanelUsers(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAdminPanelUsers_UserNotFound() {
        when(userRepository.findUsersWithRolesById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getAdminPanelUsers(1L);
        });

        assertEquals("Users not found", exception.getMessage());
    }

    @Test
    void testUpdateUserStatus_Success() {
        when(userRepository.findUserWithCredentialsById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUserStatus(1L, userStatusDto);

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserStatus_UserNotFound() {
        when(userRepository.findUserWithCredentialsById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserStatus(1L, userStatusDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_Success() {
        user.setRoles(new ArrayList<>());

        when(userRepository.findUserWithRolesAndCredentialById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleIn(anyList())).thenReturn(Collections.singletonList(role));

        userService.updateUserRoles(1L, userRoleDto);

        assertEquals(1, user.getRoles().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_RoleNotFound() {
        when(userRepository.findUserWithRolesAndCredentialById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleIn(anyList())).thenReturn(Collections.emptyList());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> {
            userService.updateUserRoles(1L, userRoleDto);
        });

        assertEquals("Role not found: [USER]", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_UserNotFound() {
        when(userRepository.findUserWithRolesAndCredentialById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserRoles(1L, userRoleDto);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}

