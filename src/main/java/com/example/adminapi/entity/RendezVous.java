package com.example.adminapi.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateHeure;

    private String lieu;

    @OneToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;
}
