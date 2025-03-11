package com.example.demo;

import com.example.demo.exception.CredentialException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Credential;
import com.example.demo.model.User;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.service.CredentialService;
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
public class CredentialServiceTest {

    @Mock
    private CredentialRepository credentialRepository;

    @InjectMocks
    private CredentialService credentialService;

    private Credential credential;
    private User user;

    @BeforeEach
    void setUp() {
        credential = new Credential();
        credential.setId(1L);
        credential.setUsername("testuser");
        credential.setPassword("password");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        credential.setUser(user);
    }

    @Test
    void testCreateCredential_Success() {
        when(credentialRepository.save(any(Credential.class))).thenReturn(credential);

        Credential result = credentialService.create(credential);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(credentialRepository, times(1)).save(any(Credential.class));
    }

    @Test
    void testCreateCredential_Exception() {
        when(credentialRepository.save(any(Credential.class))).thenThrow(new CredentialException("Failed to save"));

        CredentialException exception = assertThrows(CredentialException.class, () -> {
            credentialService.create(credential);
        });

        assertEquals("Failed to create credential", exception.getMessage());
        verify(credentialRepository, times(1)).save(any(Credential.class));
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(credentialRepository.findByUsername("testuser")).thenReturn(Optional.of(credential));

        User result = credentialService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(credentialRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(credentialRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            credentialService.loadUserByUsername("testuser");
        });

        assertEquals("User nottestuserfound", exception.getMessage());
        verify(credentialRepository, times(1)).findByUsername("testuser");
    }
}

