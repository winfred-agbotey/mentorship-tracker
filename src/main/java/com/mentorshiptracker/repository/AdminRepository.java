package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends
        JpaRepository<Admin, UUID> {
    
}
