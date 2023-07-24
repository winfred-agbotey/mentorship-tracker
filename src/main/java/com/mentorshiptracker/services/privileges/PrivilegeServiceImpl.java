package com.mentorshiptracker.services.privileges;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentorshiptracker.dtos.PermissionDTO;
import com.mentorshiptracker.dtos.RoleDTO;
import com.mentorshiptracker.exceptions.PrivilegeException;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.PermissionRepository;
import com.mentorshiptracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final ObjectMapper objectMapper;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public Permission createPermission(PermissionDTO permissionDTO) {
        Permission permission = objectMapper.convertValue(permissionDTO, Permission.class);
        boolean permissionExists = permissionRepository.existsByNameIgnoreCase(permission.getName());
        if (permissionExists) {
            throw new PrivilegeException("Permission Already Exists!");
        }
        return permissionRepository.save(permission);
    }

    @Override
    public Role createRole(RoleDTO roleDTO) {
        Role role = objectMapper.convertValue(roleDTO, Role.class);
        Set<String> permissionNames = roleDTO.getPermissions();

        Set<Permission> permissions = new HashSet<>();
        for (String permissionName : permissionNames) {
            Permission permission = permissionRepository.findByNameIgnoreCase(permissionName);
            if (permission == null) {
                throw new PrivilegeException("Permission not found: " + permissionName);
            }
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        boolean roleExists = roleRepository.existsByNameIgnoreCase(role.getName());
        if (roleExists) {
            throw new PrivilegeException("Role already Exists!");
        }
        return roleRepository.save(role);
    }
}
