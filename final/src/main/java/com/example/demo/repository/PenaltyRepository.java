package com.example.demo.repository;

import com.example.demo.model.Penalty;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"car"})
    List<Penalty> findAll();
}
