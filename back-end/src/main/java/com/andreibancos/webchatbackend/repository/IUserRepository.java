package com.andreibancos.webchatbackend.repository;

import com.andreibancos.webchatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
}
