package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"credential"})
    Optional<User> findUserWithCredentialsById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,attributePaths = {"roles","credential"})
    Optional<List<User>> findUsersWithRolesById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,attributePaths = {"roles","credential"})
    Optional<User> findUserWithRolesAndCredentialById(Long id);



}
