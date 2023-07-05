package com.mentorshiptracker.seeders;

import com.mentorshiptracker.models.Admin;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.repository.AdminRepository;
import com.mentorshiptracker.repository.PermissionRepository;
import com.mentorshiptracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.mentorshiptracker.constants.AppConstants.*;

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
        Permission manageMentorshipPermission = new Permission(MANAGE_MENTORSHIP, "create, view, update and delete on mentorship(advisors and advisees)");
        Permission viewMentorshipPermission = new Permission(VIEW_MENTORSHIP, "View mentorship only");
        //check if permissions exists
        boolean manageMentorshipPermissionExists = permissionRepository.existsByName(MANAGE_MENTORSHIP);
        boolean viewMentorshipPermissionExists = permissionRepository.existsByName(VIEW_MENTORSHIP);
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
        Role mentorshipManagerRole = new Role(MANAGER_ROLE_NAME, "Perform mentorship associated CRUD actions");
        //find permissions
        Permission manageMentorshipPermission = permissionRepository.findByName(MANAGE_MENTORSHIP);
        Permission viewMentorshipPermission = permissionRepository.findByName(VIEW_MENTORSHIP);
        //set list of permissions on role
        mentorshipManagerRole.setPermissions(Set.of(manageMentorshipPermission, viewMentorshipPermission));

        boolean mentorshipManagerRoleExists = roleRepository.existsByName(MANAGER_ROLE_NAME);
        if (!mentorshipManagerRoleExists) {
            roleRepository.save(mentorshipManagerRole);
        }

        Role administratorRole = new Role(ADMIN_ROLE_NAME, "Perform all actions");
        boolean administratorRoleExists = roleRepository.existsByName(ADMIN_ROLE_NAME);

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

        Role administratorRole = roleRepository.findByName(ADMIN_ROLE_NAME);
        Admin admin = new Admin("admin", "admin@mentor.com", "admin123");
        Optional<Admin> adminExists = adminRepository.existsByEmail(admin.getEmail());
        if (adminExists.isEmpty()) {
            admin.setRole(administratorRole);
            adminRepository.save(admin);
        }
    }

}


