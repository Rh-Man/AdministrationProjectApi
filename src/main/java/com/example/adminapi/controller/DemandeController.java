package com.example.adminapi.controller;


import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Utilisateur;
import com.example.adminapi.enums.Role;
import com.example.adminapi.enums.StatutDemande;
import com.example.adminapi.enums.TypeDocument;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.UtilisateurRepository;
import com.example.adminapi.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private final JwtService jwtService = new JwtService();

    //  Créer une demande
    @PostMapping("/ajoutDemande")
    public ResponseEntity<?> createDemande(@RequestBody Demande demande,
                                           @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        demande.setUtilisateur(utilisateur);
        demande.setDateSoumission(LocalDateTime.now());
        demande.setStatut(StatutDemande.EN_ATTENTE);
        Demande saved = demandeRepository.save(demande);

        // Accusé textuel
        Map<String, Object> accusé = new LinkedHashMap<>();
        accusé.put("message", "Demande soumise avec succès.");
        accusé.put("numéroDemande", saved.getId());
        accusé.put("typeDocument", saved.getTypeDocument());
        accusé.put("statut", saved.getStatut());
        accusé.put("dateSoumission", saved.getDateSoumission());
        accusé.put("citoyen", utilisateur.getPrenom() + " " + utilisateur.getNom());

        return ResponseEntity.ok(demandeRepository.save(demande));
    }


    // ✅ Lister toutes les demandes
    @GetMapping("listDemande")
    public ResponseEntity<?> getDemandes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Si l'utilisateur est un citoyen, on filtre par lui-même
        if (utilisateur.getRole().equals(Role.CITOYEN)) {
            List<Demande> mesDemandes = demandeRepository.findByUtilisateur(utilisateur);
            return ResponseEntity.ok(mesDemandes);
        }

        // Sinon, il peut voir toutes les demandes
        List<Demande> toutesLesDemandes = demandeRepository.findAll();
        return ResponseEntity.ok(toutesLesDemandes);
    }

    //Modifier une demande
    @PutMapping("/modifier/{id}")
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
    @DeleteMapping("/supprimer/{id}")
    public void deleteDemande(@PathVariable Long id) {
        demandeRepository.deleteById(id);
    }

    @GetMapping("/filtrer")
    public ResponseEntity<?> filtrerDemandes(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) TypeDocument typeDocument,
            @RequestParam(required = false) StatutDemande statut
    ) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Demande> demandes;

        if (utilisateur.getRole() == Role.CITOYEN) {
            demandes = demandeRepository.filtrerPourCitoyen(utilisateur.getId(), typeDocument, statut);
        } else {
            demandes = demandeRepository.filtrerGlobal(typeDocument, statut);
        }

        return ResponseEntity.ok(demandes);
    }


}
