package com.mentorshiptracker.dtos;

import com.mentorshiptracker.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {
    private String username;
    private String email;
    private Role role;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
}
