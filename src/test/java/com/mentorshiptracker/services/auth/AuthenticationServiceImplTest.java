package com.mentorshiptracker.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.*;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.*;
import com.mentorshiptracker.repository.AdvisorRepository;
import com.mentorshiptracker.repository.LocationRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import com.mentorshiptracker.utils.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.MANAGER_ROLE_NAME;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private  AdvisorRepository advisorRepository;
    @Mock
    private  RoleRepository roleRepository;
    @Mock
    private  JwtService jwtService;
    @Mock

    private  ObjectMapper objectMapper;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  LocationRepository locationRepository;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        advisorRepository.deleteAll();
    }

    @Test
    void registerAdvisor() {
        //Given
        Location location = new Location("Ghana", "takoradi");
        AdvisorDTO advisorDTO = new AdvisorDTO(
                "mawuli",
                "mawuli@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);
        Role role = new Role("Mentorship manager","Manager role");
        Advisor advisor = new Advisor();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        advisor.setRole(role);
        advisor.setLocation(location);

        //When
        when(userRepository.findUsersByEmail(advisorDTO.getEmail())).thenReturn(Optional.empty());
        when(objectMapper.convertValue(advisorDTO, Advisor.class)).thenReturn(advisor);
        when(roleRepository.findByNameIgnoreCase(MANAGER_ROLE_NAME)).thenReturn(role);
        when(passwordEncoder.encode(advisorDTO.getPassword())).thenReturn("encodedPassword");
        when(locationRepository.findByCountryAndCity(advisorDTO.getLocation().getCountry(),advisorDTO.getLocation().getCity())).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);
        when(advisorRepository.save(advisor)).thenReturn(advisor);
        when(jwtService.generateToken(advisor.getEmail())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(advisor.getEmail())).thenReturn(refreshToken);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        //Then
        assertEquals(authenticationService.registerAdvisor(advisorDTO),response);
        verify(userRepository).findUsersByEmail(advisorDTO.getEmail());
        verify(passwordEncoder).encode(advisorDTO.getPassword());
        verify(advisorRepository).save(any(Advisor.class));

    }
    @Test
    void willThrowWhenEmailIsTaken() {
        //Given
        Location location = new Location("Ghana", "takoradi");
        AdvisorDTO advisorDTO = new AdvisorDTO(
                "mawuli",
                "mawuli@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);

        when(userRepository.findUsersByEmail(advisorDTO.getEmail())).thenReturn(Optional.of(new User()));
        //then
        assertThatThrownBy(() -> authenticationService.registerAdvisor(advisorDTO))
                .isInstanceOf(UserException.class)
                .hasMessage("Email is already in use!!!");

        verify(userRepository, never()).save(any());
    }



    @Test
    void authenticate() {
        //Given
        AuthRequest authRequest = new AuthRequest("mawuli","mawuli123");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        User user = new User();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        //When
        when(userRepository.findUsersByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user.getEmail())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn(refreshToken);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        //Then
        assertEquals(authenticationService.authenticate(authRequest),response);
        verify(userRepository).findUsersByEmail(authRequest.getEmail());
    }
}