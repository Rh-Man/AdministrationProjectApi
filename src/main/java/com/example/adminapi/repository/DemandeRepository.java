package com.example.adminapi.repository;

import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByUtilisateur(Utilisateur utilisateur);
}

