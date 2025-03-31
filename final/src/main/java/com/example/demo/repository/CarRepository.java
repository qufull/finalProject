package com.example.demo.repository;

import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @EntityGraph(attributePaths = {"rentalPoint"})
    Optional<List<Car>> findAllByStatusAndRentalPointId(CarStatus status, Long rentalPointId);

    @EntityGraph(attributePaths = {"reservations.user.credential", "reservations.user.currency"})
    Optional<Car> findCarWithReservationAndUserById(Long carId);

    @EntityGraph(attributePaths = {"rentalPoint"})
    Optional<Car> findCarByIdAndRentalPointId(Long carId,Long RentalPointId);

}
