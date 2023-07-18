package com.mentorshiptracker.repository;

import com.mentorshiptracker.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByCountryAndCity(String country, String city);
}
