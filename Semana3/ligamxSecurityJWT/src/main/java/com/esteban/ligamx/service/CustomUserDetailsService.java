package com.esteban.ligamx.service;

import com.esteban.ligamx.model.AppUser;
import com.esteban.ligamx.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        appUser.getRoles().forEach(role -> {

            authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + role.getName())
            );

            role.getPermissions().forEach(permission ->
                    authorities.add(
                            new SimpleGrantedAuthority(permission.getName())
                    )
            );
        });

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(authorities)
                .build();
    }
}