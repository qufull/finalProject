package com.example.demo.repository;

import com.example.demo.model.enums.ReservationStatus;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"car","user","car.rentalPoint","user.credential"})
    Optional<Reservation> findFirstByUserIdOrderByIdDesc(Long userId);

    Optional<Reservation> findByUserAndStatus(User user, ReservationStatus status);
}
