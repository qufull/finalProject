package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "driver_licenses")
public class DriverLicens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number", nullable = false, length = 100)
    private String number;

    @Column(name = "data_of_issue", nullable = false)
    private Timestamp dataOfIssue;

    @Column(name = "data_of_expity", nullable = false)
    private Timestamp dataOfExpity;

    @Column(name = "have_b", nullable = false)
    private Boolean haveB = false;

    @Column(name = "img", nullable = false, length = Integer.MAX_VALUE)
    private String img;

}