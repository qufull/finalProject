package com.example.demo.repository;

import com.example.demo.model.enums.UserRoles;
import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRoles role);

    List<Role> findByRoleIn(List<UserRoles> roles);
}
