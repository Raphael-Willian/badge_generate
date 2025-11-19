package com.example.badge.generate.services;

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

    public AuthService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public RegisterResponse register(RegisterRequest requestRegister) {

        UserDetails userVerify = usersRepository.findByEmail(requestRegister.email());

        if (userVerify.equals(requestRegister.email())) {
            throw new UserAlreadyExistsException("Usuário: " + requestRegister.email() + "já cadastrado.");
        }

        Users newUsers = new Users(requestRegister.email(), requestRegister.password());
        usersRepository.save(newUsers);

        //generated token
        //generated bearer
        //return jwt para user
        String token = "Temporariamente"; //resta implementar generated token
        String bearer = "Bearer temporário"; //resta implementar validação de jwt

        return new RegisterResponse(token, bearer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

}
