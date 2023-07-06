package com.mentorshiptracker.repository;


import com.mentorshiptracker.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String mentorshipManager);
    Boolean existsByName(String role);
}
