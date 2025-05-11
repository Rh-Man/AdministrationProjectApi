package com.example.adminapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Paiement;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.PaiementRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementRepository paiementRepository;
    private final DemandeRepository demandeRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Paiement paiement) {
        Long demandeId = paiement.getDemande().getId();
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        paiement.setDemande(demande);
        paiement.setDatePaiement(LocalDateTime.now());
        Paiement savedPaiement = paiementRepository.save(paiement);
        return ResponseEntity.ok(savedPaiement);
    }

    @GetMapping
    public List<Paiement> getAll() {
        return paiementRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getById(@PathVariable Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        return ResponseEntity.ok(paiement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paiement> update(@PathVariable Long id, @RequestBody Paiement updatedPaiement) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        paiement.setMontant(updatedPaiement.getMontant());
        paiement.setMoyenPaiement(updatedPaiement.getMoyenPaiement());
        paiement.setStatutPaiement(updatedPaiement.getStatutPaiement());

        Paiement savedPaiement = paiementRepository.save(paiement);
        return ResponseEntity.ok(savedPaiement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paiementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
