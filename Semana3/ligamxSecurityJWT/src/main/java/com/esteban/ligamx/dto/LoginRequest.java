package com.esteban.ligamx.dto;

public record LoginRequest(
        String username,
        String password
) {
}