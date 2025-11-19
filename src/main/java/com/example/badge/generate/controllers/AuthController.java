package com.example.badge.generate.controllers;

import com.example.badge.generate.records.requests.RegisterRequest;
import com.example.badge.generate.records.responses.RegisterResponse;
import com.example.badge.generate.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody @Valid RegisterRequest requestRegister) {
        RegisterResponse register = authService.register(requestRegister);
        return ResponseEntity.status(HttpStatus.OK).build(register);
    }

    @PostMapping("/login")
    public ResponseEntity<> loginUser() {

    }


}
