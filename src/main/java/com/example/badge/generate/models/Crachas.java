package com.example.badge.generate.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.badge.generate.models.Templates;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "badges")
@Getter
@Setter
public class Crachas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId; // ou vincular a uma entidade Employee

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private Templates template;

    private String finalBadgeImageUrl; // caminho/url da imagem gerada

    private String employeeName;
    private String employeeRole;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}