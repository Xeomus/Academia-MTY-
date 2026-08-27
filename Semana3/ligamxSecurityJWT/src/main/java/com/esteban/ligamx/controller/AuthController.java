package com.esteban.ligamx.controller;

import com.esteban.ligamx.dto.LoginRequest;
import com.esteban.ligamx.dto.LoginResponse;
import com.esteban.ligamx.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService) {

        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username(),
                        request.password()
                );

        Authentication authenticationResponse =
                authenticationManager.authenticate(authenticationRequest);

        String token =
                tokenService.generateToken(authenticationResponse);

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}