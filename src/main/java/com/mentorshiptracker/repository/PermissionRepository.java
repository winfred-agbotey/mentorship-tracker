package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Permission findByNameIgnoreCase(String permission);

    boolean existsByNameIgnoreCase(String name);

}
