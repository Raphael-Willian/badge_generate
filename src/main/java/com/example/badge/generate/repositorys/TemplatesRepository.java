package com.example.badge.generate.repositorys;

import com.example.badge.generate.models.Templates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplatesRepository extends JpaRepository<Templates, Long> {
    Optional<Templates> findByName(String name);
}