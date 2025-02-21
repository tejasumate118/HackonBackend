package com.fourloop.hackon_backend.controller;

import com.fourloop.hackon_backend.dto.LoginRequest;
import com.fourloop.hackon_backend.dto.SignUpRequest;
import com.fourloop.hackon_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@PreAuthorize("permitAll()")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(Map.of("token",token));
    }
    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signup(@RequestBody SignUpRequest signUpRequest) {
        authService.signup(signUpRequest);
        return ResponseEntity.ok(Map.of("message","User registered successfully"));
    }
}
