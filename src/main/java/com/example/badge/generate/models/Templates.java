package com.example.badge.generate.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "badge_templates")
@Getter
@Setter
public class Templates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String backgroundImageUrl; // URL ou path
    private Integer nameX;
    private Integer nameY;
    private Integer roleX;
    private Integer roleY;
    private Integer photoX;
    private Integer photoY;
    private Integer photoWidth;
    private Integer photoHeight;
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}