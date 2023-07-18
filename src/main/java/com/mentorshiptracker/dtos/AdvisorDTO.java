package com.mentorshiptracker.dtos;

import com.mentorshiptracker.models.Location;
import com.mentorshiptracker.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvisorDTO {
    @NotNull(message = "username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "DOB is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "password is required")
    private String password;
    private Role role;
    @NotNull(message = "Location is required")
    private Location location;

    public AdvisorDTO(@NotNull String username, String email, @NotNull LocalDate dateOfBirth, @NotNull String password, @NotNull Location location) {
        this.username = username;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.location = location;
    }

}