package com.example.demo.service;

import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Credential;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserRoles;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;

    public UserDto create(User user) {
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UpdateUser updateUser,User user) {

        User changedUser = userRepository.findUserWithCredentialsById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        userMapper.updateUserFromUpdateUser(updateUser, changedUser);
        return userMapper.userToUserDto(userRepository.save(changedUser));
    }

    public void changePassword(ChangePasswordDto changePasswordDto,User user) {
        Credential credential = user.getCredential();
        credential.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
        credentialRepository.save(credential);
    }

    public UserDto getUser(User user) {
        return userMapper.userToUserDto(user);
    }

    public List<AdminPanelUserDto> getAdminPanelUsers(Long userId) {
        return userMapper.userToAdminPanelUserDtoList(userRepository.findUsersWithRolesById(userId)
                .orElseThrow(() -> new UserNotFoundException("Users not found")));
    }

    @Transactional
    public void updateUserStatus(Long userId, UserStatusDto userStatusDto) {
        User user = userRepository.findUserWithCredentialsById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(userStatusDto.getStatus());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRoles(Long userId, UserRoleDto dto) {
        User user = userRepository.findUserWithRolesAndCredentialById(userId)
                .orElseThrow(() ->  new UserNotFoundException("User not found"));

        List<UserRoles> rolesToUpdate = dto.getRole();

        List<Role> roles = roleRepository.findByRoleIn(rolesToUpdate);

        if(roles.size() != rolesToUpdate.size()) {
            List<UserRoles> foundRoles = roles.stream()
                    .map(Role::getRole)
                    .toList();

            List<UserRoles> missingRoles = rolesToUpdate.stream()
                    .filter(role -> !foundRoles.contains(role))
                    .toList();
            throw new RoleNotFoundException("Role not found: " + missingRoles);

        }

        user.getRoles().clear();

        user.getRoles().addAll(roles);

        userRepository.save(user);
    }
}
