package com.example.demo.model;

import com.example.demo.enums.CarStatus;
import com.example.demo.enums.CarTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "make", nullable = false, length = 30)
    private String make;

    @Column(name = "model", nullable = false, length = 30)
    private String model;

    @Column(name = "year", nullable = false, length = 5)
    private String year;

    @Column(name = "price_per_minute", nullable = false)
    private double pricePerMinute;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", columnDefinition = "car_status not null")
    private CarStatus status;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "type", columnDefinition = "car_type not null")
    private CarTypes type;

}