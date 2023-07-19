package com.mentorshiptracker.dtos;

import com.mentorshiptracker.models.Location;
import com.mentorshiptracker.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvisorResponseDTO {
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private Role role;
    private Location location;
}
