package com.example.adminapi.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import com.example.adminapi.enums.StatutDemande;
import com.example.adminapi.enums.TypeDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Demande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    private LocalDateTime dateSoumission;

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    private RendezVous rendezVous;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<Justificatif> justificatifs;

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    private Paiement paiement;
}

