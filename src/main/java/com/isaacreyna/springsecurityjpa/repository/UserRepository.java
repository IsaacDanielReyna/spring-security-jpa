package com.isaacreyna.springsecurityjpa.repository;

import com.isaacreyna.springsecurityjpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserNameOrEmail(String userName, String email);


}
