package com.example.demo;

import com.example.demo.dto.DriverLicensDto;
import com.example.demo.exception.DriverLicenseCreationException;
import com.example.demo.mapper.DriverLicensMapper;
import com.example.demo.model.DriverLicens;
import com.example.demo.model.User;
import com.example.demo.repository.DriverLicenseRepository;
import com.example.demo.service.DriverLicenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverLicenseServiceTest {
    @Mock
    private DriverLicenseRepository driverLicenseRepository;

    @Mock
    private DriverLicensMapper driverLicensMapper;

    @InjectMocks
    private DriverLicenseService driverLicenseService;

    private DriverLicensDto driverLicensDto;
    private DriverLicens driverLicens;
    private User user;

    @BeforeEach
    void setUp() {
        driverLicensDto = new DriverLicensDto();
        driverLicensDto.setNumber("123456789");
        driverLicensDto.setDataOfIssue(Timestamp.valueOf("2020-01-01 00:00:00.0"));
        driverLicensDto.setDataOfExpity(Timestamp.valueOf("2030-01-01 00:00:00.0"));

        driverLicens = new DriverLicens();
        driverLicens.setId(1L);
        driverLicens.setNumber("123456789");
        driverLicens.setDataOfIssue(Timestamp.valueOf("2020-01-01 00:00:00.0"));
        driverLicens.setDataOfExpity(Timestamp.valueOf("2030-01-01 00:00:00.0"));
        driverLicens.setHaveB(true);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void testCreateDriverLicense_Success() {
        when(driverLicensMapper.toDriverLicens(any(DriverLicensDto.class))).thenReturn(driverLicens);
        when(driverLicenseRepository.save(any(DriverLicens.class))).thenReturn(driverLicens);
        when(driverLicensMapper.toDriverLicensDto(any(DriverLicens.class))).thenReturn(driverLicensDto);

        DriverLicensDto result = driverLicenseService.createDriverLicense(driverLicensDto, user);

        assertNotNull(result);
        assertEquals("123456789", result.getNumber());
        assertThat(result.getDataOfIssue().toString()).startsWith("2020-01-01 00:00:00");
        assertThat(result.getDataOfExpity().toString()).startsWith("2030-01-01 00:00:00");
        verify(driverLicenseRepository, times(1)).save(any(DriverLicens.class));
    }

    @Test
    void testCreateDriverLicense_Exception() {
        when(driverLicensMapper.toDriverLicens(any(DriverLicensDto.class))).thenThrow(new DriverLicenseCreationException("Failed to map"));

        DriverLicenseCreationException exception = assertThrows(DriverLicenseCreationException.class, () -> {
            driverLicenseService.createDriverLicense(driverLicensDto, user);
        });

        assertEquals("Failed to create driver license for user with id: 1", exception.getMessage());
        verify(driverLicenseRepository, never()).save(any(DriverLicens.class));
    }
}
