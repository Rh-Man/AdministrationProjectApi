package com.example.adminapi.repository;

import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Utilisateur;
import com.example.adminapi.enums.StatutDemande;
import com.example.adminapi.enums.TypeDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByUtilisateur(Utilisateur utilisateur);

    
    // Filtrage pour les citoyen
    @Query("""
        SELECT d FROM Demande d
        WHERE d.utilisateur.id = :userId
        AND (:type IS NULL OR d.typeDocument = :type)
        AND (:statut IS NULL OR d.statut = :statut)
    """)
    List<Demande> filtrerPourCitoyen(
            @Param("userId") Long userId,
            @Param("type") TypeDocument type,
            @Param("statut") StatutDemande statut
    );

    // Filtrage pour les agent et superviseur
    @Query("""
        SELECT d FROM Demande d
        WHERE (:type IS NULL OR d.typeDocument = :type)
        AND (:statut IS NULL OR d.statut = :statut)
    """)
    List<Demande> filtrerGlobal(
            @Param("type") TypeDocument type,
            @Param("statut") StatutDemande statut
    );


}

