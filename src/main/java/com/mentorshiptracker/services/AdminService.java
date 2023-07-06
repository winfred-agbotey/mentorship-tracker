package com.mentorshiptracker.services;

import com.mentorshiptracker.dtos.AdminRequestDTO;
import com.mentorshiptracker.dtos.AdminResponseDTO;

public interface AdminService {
    AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO);
}
