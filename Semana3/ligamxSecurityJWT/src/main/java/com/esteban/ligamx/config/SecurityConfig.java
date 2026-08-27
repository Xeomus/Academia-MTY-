package com.esteban.ligamx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        )
                        .permitAll()
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

                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return jwtAuthenticationConverter;
    }
}