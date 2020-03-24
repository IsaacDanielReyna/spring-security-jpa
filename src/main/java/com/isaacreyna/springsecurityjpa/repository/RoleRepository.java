package com.isaacreyna.springsecurityjpa.repository;

import com.isaacreyna.springsecurityjpa.model.Role;
import com.isaacreyna.springsecurityjpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Optional<Role> findByRole(String role);
    Role findByName(String role);
}
