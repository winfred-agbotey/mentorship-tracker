package com.mentorshiptracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AdviseeDTO;
import com.mentorshiptracker.dtos.AdviseeResponseDTO;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.AdvisorResponseDTO;
import com.mentorshiptracker.exceptions.UserException;
import com.mentorshiptracker.models.*;
import com.mentorshiptracker.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.ADVISEE_ROLE_NAME;
import static com.mentorshiptracker.constants.AppConstants.MANAGER_ROLE_NAME;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdvisorRepository advisorRepository;
    @Mock
    private AdviseeRepository adviseeRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        advisorRepository.deleteAll();
    }

    @Test
    void createAdvisor() {
        //given
        Location location = new Location("Ghana", "takoradi");
        AdvisorDTO advisorDTO = new AdvisorDTO(
                "mawuli",
                "mawuli@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);
        Role role = new Role("Mentorship manager","Manager role");
        Advisor advisor = new Advisor();
        AdvisorResponseDTO results = new AdvisorResponseDTO(
                advisor.getUsername(), advisorDTO.getEmail(),advisor.getDateOfBirth(), advisor.getRole(),advisor.getLocation()
        );
        advisor.setRole(role);
        advisor.setLocation(location);

        when(userRepository.findUsersByEmail(advisorDTO.getEmail())).thenReturn(Optional.empty());
        when(objectMapper.convertValue(advisorDTO, Advisor.class)).thenReturn(advisor);
        when(roleRepository.findByNameIgnoreCase(MANAGER_ROLE_NAME)).thenReturn(role);
        when(passwordEncoder.encode(advisorDTO.getPassword())).thenReturn("encodedPassword");
        when(locationRepository.findByCountryAndCity(advisorDTO.getLocation().getCountry(),advisorDTO.getLocation().getCity())).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);
        when(advisorRepository.save(advisor)).thenReturn(advisor);
        when(objectMapper.convertValue(advisor, AdvisorResponseDTO.class)).thenReturn(results);

        assertEquals(userService.createAdvisor(advisorDTO),results);
        verify(userRepository).findUsersByEmail(advisorDTO.getEmail());
        verify(passwordEncoder).encode(advisorDTO.getPassword());
        verify(advisorRepository).save(any(Advisor.class));

    }


    @Test
    void willThrownAdvisorEmailIsTaken(){
        Location location = new Location("Ghana", "takoradi");
        AdvisorDTO advisorDTO = new AdvisorDTO(
                "mawuli",
                "mawuli@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);

        when(userRepository.findUsersByEmail(advisorDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(()->userService.createAdvisor(advisorDTO))
                .isInstanceOf(UserException.class)
                .hasMessage("Email already in use!");

        verify(advisorRepository, never()).save(any());
    }

    @Test
    void createAdvisee() {
        Location location = new Location("Togo", "lome");
        AdviseeDTO adviseeDTO = new AdviseeDTO("mawuli",
                "mawulii@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);
        Role role = new Role("Advisee","Advisee role");
        Advisee advisee = new Advisee();


        AdviseeResponseDTO responseDTO = new AdviseeResponseDTO(
                advisee.getUsername(), advisee.getEmail(),advisee.getDateOfBirth(), advisee.getRole(),advisee.getLocation()

        );
        advisee.setRole(role);
        advisee.setLocation(location);

        when(userRepository.findUsersByEmail(adviseeDTO.getEmail())).thenReturn(Optional.empty());
        when(objectMapper.convertValue(adviseeDTO, Advisee.class)).thenReturn(advisee);
        when(roleRepository.findByNameIgnoreCase(ADVISEE_ROLE_NAME)).thenReturn(role);
        when(passwordEncoder.encode(adviseeDTO.getPassword())).thenReturn("encodedPassword");
        when(locationRepository.findByCountryAndCity(adviseeDTO.getLocation().getCountry(),adviseeDTO.getLocation().getCity())).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);
        when(adviseeRepository.save(advisee)).thenReturn(advisee);
        when(objectMapper.convertValue(advisee, AdviseeResponseDTO.class)).thenReturn(responseDTO);

        assertEquals(userService.createAdvisee(adviseeDTO),responseDTO);
        verify(userRepository).findUsersByEmail(adviseeDTO.getEmail());
        verify(passwordEncoder).encode(adviseeDTO.getPassword());
        verify(adviseeRepository).save(any(Advisee.class));
    }


    @Test
    void willThrownAdviseeEmailIsTaken(){
        Location location = new Location("Ghana", "takoradi");
        AdviseeDTO adviseeDTO = new AdviseeDTO(
                "mawuli",
                "mawuli@gmail.com",
                LocalDate.of(1999, 8, 8),
                "mawuli123",
                location);

        when(userRepository.findUsersByEmail(adviseeDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(()->userService.createAdvisee(adviseeDTO))
                .isInstanceOf(UserException.class)
                .hasMessage("Email already in use!");

        verify(advisorRepository, never()).save(any());
    }
}