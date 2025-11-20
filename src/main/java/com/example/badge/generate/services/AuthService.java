package com.example.badge.generate.services;

import com.example.badge.generate.config.SecurityConfig;
import com.example.badge.generate.exceptions.UserAlreadyExistsException;
import com.example.badge.generate.models.Users;
import com.example.badge.generate.records.requests.RegisterRequest;
import com.example.badge.generate.records.responses.RegisterResponse;
import com.example.badge.generate.repositorys.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

        UserDetails userVerify = usersRepository.findByEmail(requestRegister.email());

        if (userVerify.getUsername().equals(requestRegister.email())) {
            throw new UserAlreadyExistsException("Usuário: " + requestRegister.email() + "já cadastrado.");
        }

        String encodedPassword = securityConfig.passwordEncoder().encode(requestRegister.password());

        Users newUsers = new Users(requestRegister.email(), encodedPassword);
        usersRepository.save(newUsers);

        String token = jwtService.generateToken(newUsers); //resta implementar generated token
        String bearer = "Bearer "; //resta implementar validação de jwt

        return new RegisterResponse(token, bearer);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails findUser = usersRepository.findByEmail(email);

        if(findUser == null) {
            throw new UsernameNotFoundException("Usuário não cadastrado ou digitado incorretamente.");
        }
        return findUser;
    }

}
