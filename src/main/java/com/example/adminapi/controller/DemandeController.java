package com.example.adminapi.controller;


import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Utilisateur;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // ✅ Créer une demande
    @PostMapping
    public Demande createDemande(@RequestBody Demande demande) {
        Long utilisateurId = demande.getUtilisateur().getId();

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        demande.setUtilisateur(utilisateur);

        return demandeRepository.save(demande);
    }


    // ✅ Lister toutes les demandes
    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    // ✅ Modifier une demande
    @PutMapping("/{id}")
    public Demande updateDemande(@PathVariable Long id, @RequestBody Demande updatedDemande) {
        return demandeRepository.findById(id).map(demande -> {
            demande.setTypeDocument(updatedDemande.getTypeDocument());
            demande.setDateSoumission(updatedDemande.getDateSoumission());
            demande.setStatut(updatedDemande.getStatut());
            demande.setUtilisateur(updatedDemande.getUtilisateur()); // attention à bien passer un utilisateur existant
            return demandeRepository.save(demande);
        }).orElseThrow(() -> new RuntimeException("Demande non trouvée avec id " + id));
    }

    // ✅ Supprimer une demande
    @DeleteMapping("/{id}")
    public void deleteDemande(@PathVariable Long id) {
        demandeRepository.deleteById(id);
    }

}
