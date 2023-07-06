package com.mentorshiptracker.controllers;


import com.mentorshiptracker.dtos.PermissionDTO;
import com.mentorshiptracker.dtos.RoleDTO;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.services.privileges.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/privilege")
@RequiredArgsConstructor
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @PostMapping("/role")
    public ResponseEntity<Role> createRole(@RequestBody RoleDTO roleDTO){
        Role createdRole = privilegeService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PostMapping("/permission")
    public ResponseEntity<Permission> createPermission(@RequestBody PermissionDTO permissionDTO) {
        Permission createdPermission = privilegeService.createPermission(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }
}
