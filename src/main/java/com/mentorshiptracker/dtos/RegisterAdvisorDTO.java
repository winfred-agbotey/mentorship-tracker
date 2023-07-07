package com.mentorshiptracker.dtos;

import com.mentorshiptracker.models.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdvisorDTO {
    @NotNull(message = "field is required")
    private String username;
    @NotNull(message = "field is required")
    private String email;
    @NotNull(message = "field is required")
    private String password;
    @NotNull(message = "field is required")
    private Role role;

}
