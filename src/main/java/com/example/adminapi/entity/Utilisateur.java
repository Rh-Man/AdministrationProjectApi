package com.example.adminapi.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import com.example.adminapi.enums.Role;

@Entity
@Data
public class Utilisateur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String telephone;
    private String nin;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "utilisateur")
    private List<Demande> demandes;
}
