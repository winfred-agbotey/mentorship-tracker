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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.mentorshiptracker.constants.AppConstants.ADMIN_ROLE_NAME;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AdminServiceImplTest {
    //creating a mock service to mimic that of an actual object
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ObjectMapper objectMapper;


    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createAdmin() {
        //Given
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO("admin", "admin@gmail.com", "admin123");
        Admin admin = new Admin("admin", "admin@gmail.com", "encodedPassword");
        AdminResponseDTO results = new AdminResponseDTO(
                admin.getUsername(), admin.getEmail(), admin.getRole(), admin.getDateCreated(), admin.getDateModified()
        );
        Role role = new Role("Administrator","Administrator role");

        admin.setRole(role);
        //When
        when(userRepository.findUsersByEmail(adminRequestDTO.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByNameIgnoreCase(ADMIN_ROLE_NAME)).thenReturn(role);
        when(objectMapper.convertValue(adminRequestDTO, Admin.class)).thenReturn(admin);
        when(passwordEncoder.encode(adminRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(adminRepository.save(admin)).thenReturn(admin);
        when(objectMapper.convertValue(admin, AdminResponseDTO.class)).thenReturn(results);

        assertEquals(adminService.createAdmin(adminRequestDTO),results);
        verify(userRepository).findUsersByEmail(adminRequestDTO.getEmail());
        verify(passwordEncoder).encode(adminRequestDTO.getPassword());
        verify(adminRepository).save(any(Admin.class));



    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO(

                "Jamila",
                "jamila@gmail.com",
                "password123"
        );

        when(userRepository.findUsersByEmail(adminRequestDTO.getEmail())).thenReturn(Optional.of(new User()));
        //then
        assertThatThrownBy(() -> adminService.createAdmin(adminRequestDTO))
                .isInstanceOf(UserException.class)
                .hasMessage("Email is already in use!!!");

        verify(userRepository, never()).save(any());
    }
}