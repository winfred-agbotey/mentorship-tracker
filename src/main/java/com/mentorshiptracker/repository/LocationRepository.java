package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
