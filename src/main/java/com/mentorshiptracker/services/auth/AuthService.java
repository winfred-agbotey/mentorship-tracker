package com.mentorshiptracker.services.auth;

import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AuthenticationResponse;
import com.mentorshiptracker.dtos.RegisterAdvisorDTO;

public interface AuthService {
    AuthenticationResponse registerAdvisor(RegisterAdvisorDTO requestDTO);
    AuthenticationResponse authenticate(AuthRequest authRequestDTO);
}
