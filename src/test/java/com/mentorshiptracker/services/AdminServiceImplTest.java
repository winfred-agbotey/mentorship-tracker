package com.mentorshiptracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.AdminRequestDTO;
import com.mentorshiptracker.dtos.AdminResponseDTO;
import com.mentorshiptracker.models.Admin;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.AdminRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    //creating a mock service to mimic that of an actual object
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AdminService underTest;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ObjectMapper objectMapper = new ObjectMapper();


    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        new AdminServiceImpl(objectMapper, adminRepository, roleRepository, userRepository, passwordEncoder);
//        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createAdmin() {
        //Given
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO("admin", "admin@gmail.com", "admin123");
        when(userRepository.findUsersByEmail(adminRequestDTO.getEmail())).thenReturn(Optional.empty());
        Admin admin = new Admin("admin", "admin@gmail.com", "encodedPassword");

        Role role = new Role("ADMIN", "admin role");
        when(roleRepository.findByNameIgnoreCase(role.getName())).thenReturn(role);

        //When
        when(objectMapper.convertValue(adminRequestDTO, Admin.class)).thenReturn(admin);
        when(passwordEncoder.encode(adminRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(adminRepository.save(admin)).thenReturn(admin);

//        verify(adminRepository).save();

//        Admin admin = new Admin(adminRequestDTO.getUsername(),adminRequestDTO.getEmail(),"encodedPassword");
        AdminResponseDTO results = new AdminResponseDTO(
                admin.getUsername(), admin.getEmail(), admin.getRole(), admin.getDateCreated(), admin.getDateModified()
        );
//        assertThat(admin).isEqualTo(adminRequestDTO);
        assertEquals(results, underTest.createAdmin(adminRequestDTO));

    }

    @Test
    @Disabled
    void willThrowWhenEmailIsTaken() {
    }
}