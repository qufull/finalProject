package com.example.demo.repository;

import com.example.demo.model.Credential;
import com.example.demo.model.DriverLicens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicens, Long> {
}
