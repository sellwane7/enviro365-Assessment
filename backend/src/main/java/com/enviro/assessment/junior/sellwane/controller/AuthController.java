package com.enviro.assessment.junior.sellwane.controller;

import com.enviro.assessment.junior.sellwane.dto.LoginRequest;
import com.enviro.assessment.junior.sellwane.dto.LoginResponse;
import com.enviro.assessment.junior.sellwane.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
