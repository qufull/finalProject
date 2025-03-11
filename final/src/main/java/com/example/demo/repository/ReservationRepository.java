package com.example.demo.repository;

import com.example.demo.enums.ReservationStatus;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"car","user"})
    Optional<Reservation> findFirstByUserIdOrderByIdDesc(Long userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"car"})
    Optional<Reservation> findFirstByCarIdOrderByIdDesc(Long userId);

    Optional<Reservation> findByUserAndStatus(User user, ReservationStatus status);
}
