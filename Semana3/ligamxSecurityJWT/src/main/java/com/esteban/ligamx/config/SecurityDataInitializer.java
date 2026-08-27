package com.esteban.ligamx.config;

import com.esteban.ligamx.model.AppUser;
import com.esteban.ligamx.model.Permission;
import com.esteban.ligamx.model.Role;
import com.esteban.ligamx.repository.AppUserRepository;
import com.esteban.ligamx.repository.PermissionRepository;
import com.esteban.ligamx.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SecurityDataInitializer {

    @Bean
    CommandLineRunner initSecurityData(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            Permission teamRead = permissionRepository.findByName("TEAM_READ")
                    .orElseGet(() ->
                            permissionRepository.save(
                                    new Permission("TEAM_READ")
                            )
                    );

            Permission teamCreate = permissionRepository.findByName("TEAM_CREATE")
                    .orElseGet(() ->
                            permissionRepository.save(
                                    new Permission("TEAM_CREATE")
                            )
                    );

            Permission teamUpdate = permissionRepository.findByName("TEAM_UPDATE")
                    .orElseGet(() ->
                            permissionRepository.save(
                                    new Permission("TEAM_UPDATE")
                            )
                    );

            Permission teamDelete = permissionRepository.findByName("TEAM_DELETE")
                    .orElseGet(() ->
                            permissionRepository.save(
                                    new Permission("TEAM_DELETE")
                            )
                    );

            Permission playerRead =
                    permissionRepository.findByName("PLAYER_READ")
                            .orElseGet(() ->
                                    permissionRepository.save(
                                            new Permission("PLAYER_READ")
                                    )
                            );

            Permission playerCreate =
                    permissionRepository.findByName("PLAYER_CREATE")
                            .orElseGet(() ->
                                    permissionRepository.save(
                                            new Permission("PLAYER_CREATE")
                                    )
                            );

            Permission playerUpdate =
                    permissionRepository.findByName("PLAYER_UPDATE")
                            .orElseGet(() ->
                                    permissionRepository.save(
                                            new Permission("PLAYER_UPDATE")
                                    )
                            );

            Permission playerDelete =
                    permissionRepository.findByName("PLAYER_DELETE")
                            .orElseGet(() ->
                                    permissionRepository.save(
                                            new Permission("PLAYER_DELETE")
                                    )
                            );

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> new Role("ADMIN"));

            adminRole.setPermissions(new HashSet<>(Set.of(
                    teamRead,
                    teamCreate,
                    teamUpdate,
                    teamDelete,

                    playerRead,
                    playerCreate,
                    playerUpdate,
                    playerDelete
            )));

            adminRole = roleRepository.save(adminRole);

            Role viewerRole = roleRepository.findByName("VIEWER")
                    .orElseGet(() -> new Role("VIEWER"));

            viewerRole.setPermissions(new HashSet<>(Set.of(
                    teamRead,
                    playerRead
            )));

            viewerRole = roleRepository.save(viewerRole);

            if (appUserRepository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser(
                        "admin",
                        passwordEncoder.encode("admin123")
                );

                admin.setRoles(new HashSet<>(Set.of(adminRole)));

                appUserRepository.save(admin);
            }

            if (appUserRepository.findByUsername("viewer").isEmpty()) {
                AppUser viewer = new AppUser(
                        "viewer",
                        passwordEncoder.encode("viewer123")
                );

                viewer.setRoles(new HashSet<>(Set.of(viewerRole)));

                appUserRepository.save(viewer);
            }
        };
    }
}