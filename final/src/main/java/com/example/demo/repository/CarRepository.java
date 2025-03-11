package com.example.demo.repository;

import com.example.demo.enums.CarStatus;
import com.example.demo.model.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    @EntityGraph(attributePaths = {})
    Optional<List<Car>> findAllByStatus(CarStatus status);

    @EntityGraph(attributePaths = {"reservations.user.credential", "reservations.user.currency"})
    Optional<Car> findCarWithReservationAndUserById(Long carId);
}
