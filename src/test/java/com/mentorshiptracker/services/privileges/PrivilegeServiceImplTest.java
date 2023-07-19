package com.mentorshiptracker.services.privileges;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.PermissionDTO;
import com.mentorshiptracker.dtos.RoleDTO;
import com.mentorshiptracker.exceptions.PrivilegeException;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.PermissionRepository;
import com.mentorshiptracker.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrivilegeServiceImplTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private PrivilegeServiceImpl privilegeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
    }

    @Test
    void createPermission() {
        //Given
        PermissionDTO permissionDTO = new PermissionDTO("Test permission","Test description");
        Permission permission = new Permission(permissionDTO.getName(),permissionDTO.getDescription());

        //When
        when(objectMapper.convertValue(permissionDTO, Permission.class)).thenReturn(permission);
        when(permissionRepository.existsByNameIgnoreCase(permission.getName())).thenReturn(Boolean.FALSE);
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        assertEquals(privilegeService.createPermission(permissionDTO),permission);
        verify(permissionRepository).existsByNameIgnoreCase(permissionDTO.getName());

    }

    @Test
    void createRole() {
        Set<String> permissionNames = new HashSet<>();
        permissionNames.add("CREATE_USER");
        permissionNames.add("DELETE_USER");
        RoleDTO roleDTO = new RoleDTO("ADMIN","Role description",permissionNames);

        Role role = new Role(
                roleDTO.getName(),roleDTO.getDescription()
        );

        Permission permission1 = new Permission();
        permission1.setName("CREATE_USER");

        Permission permission2 = new Permission();
        permission2.setName("DELETE_USER");

        //When
        when(objectMapper.convertValue(roleDTO, Role.class)).thenReturn(role);
        when(permissionRepository.findByNameIgnoreCase("CREATE_USER")).thenReturn(permission1);
        when(permissionRepository.findByNameIgnoreCase("DELETE_USER")).thenReturn(permission2);
        when(roleRepository.existsByNameIgnoreCase(role.getName())).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        assertEquals(privilegeService.createRole(roleDTO),role);
        assertEquals(2, privilegeService.createRole(roleDTO).getPermissions().size());

    }
}