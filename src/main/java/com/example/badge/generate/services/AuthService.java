package com.example.badge.generate.services;

import com.example.badge.generate.config.SecurityConfig;
import com.example.badge.generate.exceptions.UserAlreadyExistsException;
import com.example.badge.generate.models.Users;
import com.example.badge.generate.records.requests.LoginRequest;
import com.example.badge.generate.records.requests.RegisterRequest;
import com.example.badge.generate.records.responses.LoginResponse;
import com.example.badge.generate.records.responses.RegisterResponse;
import com.example.badge.generate.repositorys.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final SecurityConfig securityConfig;

    public AuthService(UsersRepository usersRepository, JwtService jwtService, SecurityConfig securityConfig) {
        this.usersRepository = usersRepository;
        this.jwtService = jwtService;
        this.securityConfig = securityConfig;
    }

    public RegisterResponse register(RegisterRequest requestRegister) {

        Users userVerify = usersRepository.findByEmail(requestRegister.email());

        // Se já existe → ERRO
        if (userVerify != null) {
            throw new UserAlreadyExistsException("Usuário já cadastrado!");
        }

        // Cria o novo usuário
        String encodedPassword = securityConfig.passwordEncoder().encode(requestRegister.password());

        Users newUser = new Users(requestRegister.email(), encodedPassword);
        usersRepository.save(newUser);

        // Gera o token
        String token = jwtService.generateToken(newUser);
        String bearer = "Bearer ";

        return new RegisterResponse(token, bearer);
    }


    public LoginResponse login(LoginRequest request) {

        Users userLogin = usersRepository.findByEmail(request.email());

        if(userLogin == null) {
            throw new UsernameNotFoundException("Usuário " + request.email() + "não existe.");
        }
        else if (!securityConfig.passwordEncoder().matches(request.password(), userLogin.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }
        String token = jwtService.generateToken(userLogin);
        String bearer = "Bearer ";

        return new LoginResponse(token, bearer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersRepository.findByEmail(username); // OU findByUsername

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())   // ou getUsername()
                .password(user.getPassword())
                .authorities("USER")             // pode pegar do próprio user
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
