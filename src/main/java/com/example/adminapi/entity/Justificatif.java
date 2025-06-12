package com.example.adminapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Justificatif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String cheminFichier;

    @ManyToOne
    @JoinColumn(name = "demande_id")
    @JsonIgnoreProperties("justificatifs") // ignore le champ "justificatifs" dans demande
    private Demande demande;
}

