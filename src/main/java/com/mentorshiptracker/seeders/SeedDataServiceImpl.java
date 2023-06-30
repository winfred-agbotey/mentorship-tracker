package com.mentorshiptracker.seeders;

import com.mentorshiptracker.models.Admin;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.AdminRepository;
import com.mentorshiptracker.repository.PermissionRepository;
import com.mentorshiptracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeedDataServiceImpl implements SeedDataService {
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void seedDatabase() {
        seedPermissions();
        seedRoles();
        seedAdmins();

    }

    @Override
    public void seedPermissions() {
        //Instantiate new permissions
        Permission manageMentorshipPermission = new Permission("Manage mentorship", "create, view, update and delete on mentorship(advisors and advisees)");
        Permission viewMentorshipPermission = new Permission("View mentorship", "View mentorship only");
        //check if permissions exists
        boolean manageMentorshipPermissionExists = permissionRepository.existsByName("Manage mentorship");
        boolean viewMentorshipPermissionExists = permissionRepository.existsByName("View mentorship");
        if (!manageMentorshipPermissionExists) {
            permissionRepository.save(manageMentorshipPermission);
        }
        if (!viewMentorshipPermissionExists) {
            permissionRepository.save(viewMentorshipPermission);
        }
    }

    @Override
    public void seedRoles() {
        //Instantiate a new manager role
        Role mentorshipManagerRole = new Role("Mentorship manager", "Perform mentorship associated CRUD actions");
        //find permissions
        Permission manageMentorshipPermission = permissionRepository.findByName("Manage mentorship");
        Permission viewMentorshipPermission = permissionRepository.findByName("View mentorship");
        //set list of permissions on role
        mentorshipManagerRole.setPermissions(List.of(manageMentorshipPermission, viewMentorshipPermission));

        boolean mentorshipManagerRoleExists = roleRepository.existsByName("Mentorship manager");
        if (!mentorshipManagerRoleExists) {
            roleRepository.save(mentorshipManagerRole);
        }

        Role administratorRole = new Role("Administrator", "Perform all actions");
        boolean administratorRoleExists = roleRepository.existsByName("Administrator");

        if (!administratorRoleExists) {
            roleRepository.save(administratorRole);
        }
    }

    @Override
    public void seedAdmins() {
        /*
        1.find admin role
        2.Instantiate a new admin
        3.check if admin already exists
        3. set admin Role and save
        */

        Role administratorRole = roleRepository.findByName("Administrator");
        Admin admin = new Admin("admin", "admin@mentor.com", "admin123");
        boolean adminExists = adminRepository.existsByEmail(admin.getEmail());
        if (!adminExists){
            admin.setRole(administratorRole);
            adminRepository.save(admin);
        }
    }

}


