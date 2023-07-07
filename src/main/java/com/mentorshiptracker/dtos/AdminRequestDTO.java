package com.mentorshiptracker.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDTO {
    @NotNull(message = "field is required")
    private String username;
    @NotNull(message = "field is required")
    private String email;
    @NotNull(message = "field is required")
    private String password;
}
