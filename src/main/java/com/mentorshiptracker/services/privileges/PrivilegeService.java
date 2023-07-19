package com.mentorshiptracker.services.privileges;


import com.mentorshiptracker.dtos.PermissionDTO;
import com.mentorshiptracker.dtos.RoleDTO;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;

public interface PrivilegeService {

    Role createRole(RoleDTO roleDTO);

    Permission createPermission(PermissionDTO permissionDTO);
}
