package com.mentorshiptracker.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AuthenticationResponse;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.Advisor;
import com.mentorshiptracker.models.Location;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.models.User;
import com.mentorshiptracker.repository.AdvisorRepository;
import com.mentorshiptracker.repository.LocationRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import com.mentorshiptracker.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.MANAGER_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AdvisorRepository advisorRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final LocationRepository locationRepository;

    @Override
    public AuthenticationResponse registerAdvisor(AdvisorDTO requestDTO) {
        userRepository.findUsersByEmail(requestDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserException("Email is already in use!!!");
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
        String accessToken = jwtService.generateToken(advisorEntity.getEmail());
        String refreshToken = jwtService.generateRefreshToken(advisorEntity.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthRequest authRequestDTO) {
        //authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getEmail(),
                        authRequestDTO.getPassword()
                )
        );

        User user = userRepository.findUsersByEmail(authRequestDTO.getEmail())
                .orElseThrow();
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
