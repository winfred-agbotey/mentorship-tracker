package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUsersByEmail(String username);


}
