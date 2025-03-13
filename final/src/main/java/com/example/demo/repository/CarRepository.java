package com.example.demo.repository;

import com.example.demo.enums.CarStatus;
import com.example.demo.model.Car;
import com.example.demo.model.RentalPoint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    @EntityGraph(attributePaths = {"rentalPoint"})
    Optional<List<Car>> findAllByStatusAndRentalPointId(CarStatus status, Long rentalPointId);

    @EntityGraph(attributePaths = {"reservations.user.credential", "reservations.user.currency"})
    Optional<Car> findCarWithReservationAndUserById(Long carId);

    @EntityGraph(attributePaths = {"rentalPoint"})
    Optional<Car> findCarByIdAndRentalPointId(Long carId,Long RentalPointId);
}
