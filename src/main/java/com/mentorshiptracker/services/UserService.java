package com.mentorshiptracker.services;

import com.mentorshiptracker.dtos.AdviseeDTO;
import com.mentorshiptracker.dtos.AdviseeResponseDTO;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.AdvisorResponseDTO;

public interface UserService {
    AdvisorResponseDTO createAdvisor(AdvisorDTO requestDTO);
    AdviseeResponseDTO createAdvisee(AdviseeDTO requestDTO);

}
