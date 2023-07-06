package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdvisorRepository extends JpaRepository<Advisor, UUID> {
}
