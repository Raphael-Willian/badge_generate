package com.example.badge.generate.controllers;

import com.example.badge.generate.records.requests.LoginRequest;
import com.example.badge.generate.records.requests.RegisterRequest;
import com.example.badge.generate.records.responses.LoginResponse;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody @Valid RegisterRequest requestRegister) {
        RegisterResponse regist = authService.register(requestRegister);
        return ResponseEntity.status(HttpStatus.OK).body(regist);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest requestLogin) {
        LoginResponse userLogin = authService.login(requestLogin);
        return ResponseEntity.status(HttpStatus.OK).body(userLogin);
    }


}
