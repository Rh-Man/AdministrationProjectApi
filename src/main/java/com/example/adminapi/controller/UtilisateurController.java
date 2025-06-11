package com.example.adminapi.controller;


import com.example.adminapi.entity.Utilisateur;
import com.example.adminapi.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*") 
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Créer un utilisateur
    @PostMapping("/ajouter")
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    // Récupérer tous les utilisateurs
    @GetMapping("/recuperer")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @PutMapping("/modifier/{id}")
    public Utilisateur updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur updatedUtilisateur) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateur.setNom(updatedUtilisateur.getNom());
            utilisateur.setPrenom(updatedUtilisateur.getPrenom());
            utilisateur.setEmail(updatedUtilisateur.getEmail());
            utilisateur.setMotDePasse(updatedUtilisateur.getMotDePasse());
            utilisateur.setRole(updatedUtilisateur.getRole());
            return utilisateurRepository.save(utilisateur);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec id " + id));
    }

    @DeleteMapping("/supprimer/{id}")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
    }

}

