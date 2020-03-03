package com.isaacreyna.springsecurityjpa.repository;

import com.isaacreyna.springsecurityjpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
}
