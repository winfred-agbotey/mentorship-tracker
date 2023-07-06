package com.mentorshiptracker.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AuthenticationResponse;
import com.mentorshiptracker.dtos.RegisterAdvisorDTO;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.Advisor;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.AdvisorRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import com.mentorshiptracker.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public AuthenticationResponse registerAdvisor(RegisterAdvisorDTO requestDTO) {
        userRepository.findUsersByEmail(requestDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserException("Email already in use");
                });
        Advisor advisorEntity = objectMapper.convertValue(requestDTO, Advisor.class);
        Role administratorRole = roleRepository.findByName(MANAGER_ROLE_NAME);
        advisorEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        advisorEntity.setRole(administratorRole);
        advisorRepository.save(advisorEntity);
        var jwtToken = jwtService.generateToken(advisorEntity.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
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

        var user = userRepository.findUsersByEmail(authRequestDTO.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
