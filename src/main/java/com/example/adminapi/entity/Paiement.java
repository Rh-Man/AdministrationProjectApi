package com.example.adminapi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import com.example.adminapi.enums.MoyenPaiement;
import com.example.adminapi.enums.StatutPaiement;


@Entity
@Data
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePaiement;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private MoyenPaiement moyenPaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statutPaiement;

    @OneToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;
}
