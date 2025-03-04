package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLicensDto {

    private String number;

    private Timestamp dataOfIssue;

    private Timestamp dataOfExpity;
}
