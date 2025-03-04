package com.example.demo.repository;

import com.example.demo.model.Credential;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,attributePaths = "user.roles")
    Optional<Credential> findByUsername (String username);
}
