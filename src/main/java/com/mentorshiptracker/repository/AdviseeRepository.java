package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Advisee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdviseeRepository extends JpaRepository<Advisee, UUID> {
}
