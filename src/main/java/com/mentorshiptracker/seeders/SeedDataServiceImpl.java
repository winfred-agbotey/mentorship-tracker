package com.mentorshiptracker.seeders;

import com.mentorshiptracker.models.Admin;
import com.mentorshiptracker.models.Permission;
import com.mentorshiptracker.models.Role;
import com.mentorshiptracker.models.User;
import com.mentorshiptracker.repository.AdminRepository;
import com.mentorshiptracker.repository.PermissionRepository;
import com.mentorshiptracker.repository.RoleRepository;
import com.mentorshiptracker.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.mentorshiptracker.constants.AppConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeedDataServiceImpl implements SeedDataService {
    Dotenv  dotenv = Dotenv.load();
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

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
        Permission adminPermission = new Permission(ADMIN_PERMISSION,"manage entire system");
        //check if permissions exists
        boolean manageMentorshipPermissionExists = permissionRepository.existsByNameIgnoreCase(MANAGE_MENTORSHIP);
        boolean viewMentorshipPermissionExists = permissionRepository.existsByNameIgnoreCase(VIEW_MENTORSHIP);
        boolean adminPermissionExists = permissionRepository.existsByNameIgnoreCase(ADMIN_PERMISSION);
        if (!adminPermissionExists){
            permissionRepository.save(adminPermission);
        }
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
        Permission manageMentorshipPermission = permissionRepository.findByNameIgnoreCase(MANAGE_MENTORSHIP);
        Permission viewMentorshipPermission = permissionRepository.findByNameIgnoreCase(VIEW_MENTORSHIP);
        Permission adminPermission = permissionRepository.findByNameIgnoreCase(ADMIN_PERMISSION);
        //set list of permissions on role
        mentorshipManagerRole.setPermissions(Set.of(manageMentorshipPermission, viewMentorshipPermission));

        boolean mentorshipManagerRoleExists = roleRepository.existsByNameIgnoreCase(MANAGER_ROLE_NAME);
        if (!mentorshipManagerRoleExists) {
            roleRepository.save(mentorshipManagerRole);
        }

        Role administratorRole = new Role(ADMIN_ROLE_NAME, "Perform all actions");
        administratorRole.setPermissions(Set.of(manageMentorshipPermission,viewMentorshipPermission,adminPermission));
        boolean administratorRoleExists = roleRepository.existsByNameIgnoreCase(ADMIN_ROLE_NAME);
        if (!administratorRoleExists) {
            roleRepository.save(administratorRole);
        }

        Role adviseeRole = new Role(ADVISEE_ROLE_NAME, "Perform all advisee actions");
        boolean adviseeRoleExists = roleRepository.existsByNameIgnoreCase(ADVISEE_ROLE_NAME);
        if (!adviseeRoleExists) {
            roleRepository.save(adviseeRole);
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

        Role administratorRole = roleRepository.findByNameIgnoreCase(ADMIN_ROLE_NAME);
        Admin admin = new Admin(dotenv.get("ADMIN_USERNAME"), dotenv.get("ADMIN_EMAIL"), passwordEncoder.encode(dotenv.get("ADMIN_PASSWORD")));
        Optional<User> adminExists = userRepository.findUsersByEmail(admin.getEmail());
        if (adminExists.isEmpty()) {
            admin.setRole(administratorRole);
            adminRepository.save(admin);
        }
    }

}


