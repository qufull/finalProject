package com.example.demo.service;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.UpdateUser;
import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Credential;
import com.example.demo.model.User;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;

    public UserDto create(User user) {
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UpdateUser updateUser,User user) {

        User changedUser = userRepository.findUserWithCredentialsById(user.getId()).orElseThrow(() ->
                new RuntimeException("User not found"));

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
}
