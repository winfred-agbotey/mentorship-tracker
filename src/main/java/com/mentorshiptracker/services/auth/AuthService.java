package com.mentorshiptracker.services.auth;

import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AuthenticationResponse;
import com.mentorshiptracker.dtos.AdvisorDTO;

public interface AuthService {
    AuthenticationResponse registerAdvisor(AdvisorDTO requestDTO);
    AuthenticationResponse authenticate(AuthRequest authRequestDTO);
}
