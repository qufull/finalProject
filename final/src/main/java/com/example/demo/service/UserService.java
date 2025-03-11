package com.example.demo.service;

import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.enums.UserRoles;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Credential;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;

    public UserDto create(User user) {
        log.info("Creating new user: {}", user);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UpdateUser updateUser,User user) {

        log.info("Updating user with id: {}", user.getId());

        User changedUser = userRepository.findUserWithCredentialsById(user.getId()).orElseThrow(() ->
        {
            log.error("User not found with id: {}", user.getId());
            return new UserNotFoundException("User not found");
        });

        userMapper.updateUserFromUpdateUser(updateUser, changedUser);
        return userMapper.userToUserDto(userRepository.save(changedUser));
    }

    public void changePassword(ChangePasswordDto changePasswordDto,User user) {

        log.info("Changing password for user with id: {}", user.getId());

        Credential credential = user.getCredential();
        credential.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
        credentialRepository.save(credential);
        log.info("Password changed successfully for user with id: {}", user.getId());
    }

    public UserDto getUser(User user) {
        log.info("Fetching user with id: {}", user.getId());
        return userMapper.userToUserDto(user);
    }

    public List<AdminPanelUserDto> getAdminPanelUsers(Long userId) {
        log.info("Fetching admin panel users for admin with id: {}", userId);
        return userMapper.userToAdminPanelUserDtoList(userRepository.findUsersWithRolesById(userId)
                .orElseThrow(() -> {
                    log.error("Users not found for admin with id: {}", userId);
                    return new UserNotFoundException("Users not found");
                }));
    }

    @Transactional
    public void updateUserStatus(Long userId, UserStatusDto userStatusDto) {
        log.info("Updating status for user with id: {}", userId);
        User user = userRepository.findUserWithCredentialsById(userId)
                .orElseThrow(() ->{
                    log.error("User not found with id: {}", userId);
                    return new UserNotFoundException("User not found");
                });

        user.setStatus(userStatusDto.getStatus());
        userRepository.save(user);
        log.info("Updated status for user with id: {} to {}", userId, userStatusDto.getStatus());
    }

    @Transactional
    public void updateUserRoles(Long userId, UserRoleDto dto) {
        log.info("Updating roles for user with id: {}", userId);
        User user = userRepository.findUserWithRolesAndCredentialById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new UserNotFoundException("User not found");
                });

        List<UserRoles> rolesToUpdate = dto.getRole();

        List<Role> roles = roleRepository.findByRoleIn(rolesToUpdate);

        if(roles.size() != rolesToUpdate.size()) {
            List<UserRoles> foundRoles = roles.stream()
                    .map(Role::getRole)
                    .toList();

            List<UserRoles> missingRoles = rolesToUpdate.stream()
                    .filter(role -> !foundRoles.contains(role))
                    .toList();
            log.error("Role(s) not found: {}", missingRoles);
            throw new RoleNotFoundException("Role not found: " + missingRoles);

        }

        user.getRoles().clear();

        user.getRoles().addAll(roles);

        userRepository.save(user);
        log.info("Updated roles for user with id: {} to {}", userId, roles);
    }
}
