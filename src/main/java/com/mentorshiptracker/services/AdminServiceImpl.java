package com.mentorshiptracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AdminRequestDTO;
import com.mentorshiptracker.dtos.AdminResponseDTO;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.Admin;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.models.User;
import com.mentorshiptracker.repository.AdminRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.ADMIN_ROLE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final ObjectMapper objectMapper;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        Optional<User> adminExists = userRepository.findUsersByEmail(adminRequestDTO.getEmail());
        if (adminExists.isPresent()) {
            log.error("Email is Already in use");
            throw new UserException("Email is already in use!!!");
        }
        Role administratorRole = roleRepository.findByName(ADMIN_ROLE_NAME);
        Admin admin = objectMapper.convertValue(adminRequestDTO, Admin.class);
        admin.setPassword(passwordEncoder.encode(adminRequestDTO.getPassword()));
        admin.setRole(administratorRole);
        Admin newAdmin = adminRepository.save(admin);
        System.out.println(newAdmin.getRole());
        System.out.println(newAdmin.getUsername());
        log.info("Admin successfully created...");
        return new AdminResponseDTO(
                admin.getUsername(), admin.getEmail(), admin.getRole(), admin.getDateCreated(), admin.getDateModified()
        );

//        System.out.println(objectMapper.convertValue(newAdmin, AdminResponseDTO.class));
//        return objectMapper.convertValue(newAdmin, AdminResponseDTO.class);
    }
}
