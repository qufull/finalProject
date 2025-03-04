package com.example.demo.service;

import com.example.demo.dto.DriverLicensDto;
import com.example.demo.mapper.DriverLicensMapper;
import com.example.demo.model.DriverLicens;
import com.example.demo.model.User;
import com.example.demo.repository.DriverLicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverLicenseService {
    private final DriverLicenseRepository driverLicenseRepository;
    private final DriverLicensMapper driverLicensMapper;

    public DriverLicensDto createDriverLicense(DriverLicensDto dto,User user) {
        DriverLicens driverLicens = driverLicensMapper.toDriverLicens(dto);
        driverLicens.setUser(user);
        driverLicens.setHaveB(true);
        driverLicens.setImg("noImg");
        return driverLicensMapper.toDriverLicensDto(driverLicenseRepository.save(driverLicens));
    }

}
