package com.example.demo.service;

import com.example.demo.exception.CarException;
import com.example.demo.exception.CredentialException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Credential;
import com.example.demo.model.User;
import com.example.demo.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialService implements UserDetailsService {
    private final CredentialRepository credentialRepository;

    public Credential create(Credential credential) {
        Credential savedCred = credentialRepository.save(credential);
        return savedCred;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException("User not" + username + "found"))
                .getUser();
    }
}
