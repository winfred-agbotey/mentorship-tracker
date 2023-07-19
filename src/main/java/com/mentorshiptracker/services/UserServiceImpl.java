package com.mentorshiptracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AdviseeDTO;
import com.mentorshiptracker.dtos.AdviseeResponseDTO;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.AdvisorResponseDTO;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.Advisee;
import com.mentorshiptracker.models.Advisor;
import com.mentorshiptracker.models.Location;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.ADVISEE_ROLE_NAME;
import static com.mentorshiptracker.constants.AppConstants.MANAGER_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AdvisorRepository advisorRepository;
    private final AdviseeRepository adviseeRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;

    @Override
    public AdvisorResponseDTO createAdvisor(AdvisorDTO requestDTO) {
        userRepository.findUsersByEmail(requestDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserException("Email already in use!");
                });
        Advisor advisorEntity = objectMapper.convertValue(requestDTO, Advisor.class);
        Role managerRole = roleRepository.findByNameIgnoreCase(MANAGER_ROLE_NAME);
        advisorEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        advisorEntity.setRole(managerRole);
        Optional<Location> locationOptional = locationRepository.findByCountryAndCity(requestDTO.getLocation().getCountry(), requestDTO.getLocation().getCity());
        Location location = locationOptional.orElseGet(() -> {
            Location newLocation = new Location(requestDTO.getLocation().getCountry(), requestDTO.getLocation().getCity());
            return locationRepository.save(newLocation);
        });
        advisorEntity.setLocation(location);
        advisorRepository.save(advisorEntity);
        return objectMapper.convertValue(advisorEntity, AdvisorResponseDTO.class);
    }

    @Override
    public AdviseeResponseDTO createAdvisee(AdviseeDTO requestDTO) {
        userRepository.findUsersByEmail(requestDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserException("Email already in use!");
                });
        Advisee adviseeEntity = objectMapper.convertValue(requestDTO,Advisee.class);
        Role adviseeRole = roleRepository.findByNameIgnoreCase(ADVISEE_ROLE_NAME);
        adviseeEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        adviseeEntity.setRole(adviseeRole);
        Optional<Location> locationOptional = locationRepository.findByCountryAndCity(requestDTO.getLocation().getCountry(), requestDTO.getLocation().getCity());
        Location location = locationOptional.orElseGet(() -> {
            Location newLocation = new Location(requestDTO.getLocation().getCountry(), requestDTO.getLocation().getCity());
            return locationRepository.save(newLocation);
        });
        adviseeEntity.setLocation(location);
        adviseeRepository.save(adviseeEntity);
        return objectMapper.convertValue(adviseeEntity, AdviseeResponseDTO.class);
    }
}
