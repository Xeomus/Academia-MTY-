package com.esteban.ligamx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Players asociados a Team
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/teams/*/players"
                        )
                        .hasAuthority("PLAYER_READ")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/teams/*/players"
                        )
                        .hasAuthority("PLAYER_CREATE")

                        // Players
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/players/**"
                        )
                        .hasAuthority("PLAYER_READ")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/players/**"
                        )
                        .hasAuthority("PLAYER_UPDATE")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/players/**"
                        )
                        .hasAuthority("PLAYER_DELETE")

                        // Teams
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/teams/**"
                        )
                        .hasAuthority("TEAM_READ")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/teams/**"
                        )
                        .hasAuthority("TEAM_CREATE")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/teams/**"
                        )
                        .hasAuthority("TEAM_UPDATE")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/teams/**"
                        )
                        .hasAuthority("TEAM_DELETE")

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
}