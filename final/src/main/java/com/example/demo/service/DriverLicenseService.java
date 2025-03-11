package com.example.demo.service;

import com.example.demo.dto.DriverLicensDto;
import com.example.demo.exception.DriverLicenseCreationException;
import com.example.demo.mapper.DriverLicensMapper;
import com.example.demo.model.DriverLicens;
import com.example.demo.model.User;
import com.example.demo.repository.DriverLicenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverLicenseService {
    private final DriverLicenseRepository driverLicenseRepository;
    private final DriverLicensMapper driverLicensMapper;

    public DriverLicensDto createDriverLicense(DriverLicensDto dto,User user) {
        log.info("Creating driver license for user with id: {}", user.getId());
        try {
            DriverLicens driverLicens = driverLicensMapper.toDriverLicens(dto);
            driverLicens.setUser(user);
            driverLicens.setHaveB(true);
            DriverLicens savedDriverLicens = driverLicenseRepository.save(driverLicens);
            log.info("Driver license created successfully for user with id: {}", user.getId());

            return driverLicensMapper.toDriverLicensDto(savedDriverLicens);
        } catch (DriverLicenseCreationException e) {
            log.error("Error creating driver license for user with id: {}", user.getId());
            throw new DriverLicenseCreationException("Failed to create driver license for user with id: " + user.getId());
        }
    }

}
