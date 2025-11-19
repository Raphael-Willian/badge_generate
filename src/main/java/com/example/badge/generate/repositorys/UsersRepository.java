package com.example.badge.generate.repositorys;

import com.example.badge.generate.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserDetails, UUID> {
    UserDetails findByEmail(String email);
}
