package com.mentorshiptracker.dtos;

import com.mentorshiptracker.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdvisorDTO {
    private String username;
    private String email;
    private String password;
    private Role role;

}
