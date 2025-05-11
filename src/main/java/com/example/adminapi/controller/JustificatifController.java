package com.example.adminapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Justificatif;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.JustificatifRepository;

import java.util.List;

@RestController
@RequestMapping("/api/justificatifs")
@RequiredArgsConstructor
public class JustificatifController {

    private final JustificatifRepository justificatifRepository;
    private final DemandeRepository demandeRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Justificatif justificatif) {
        Long demandeId = justificatif.getDemande().getId();
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        justificatif.setDemande(demande);
        Justificatif savedJustificatif = justificatifRepository.save(justificatif);
        return ResponseEntity.ok(savedJustificatif);
    }

    @GetMapping
    public List<Justificatif> getAll() {
        return justificatifRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Justificatif> getById(@PathVariable Long id) {
        Justificatif justificatif = justificatifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificatif non trouvé"));
        return ResponseEntity.ok(justificatif);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Justificatif> update(@PathVariable Long id, @RequestBody Justificatif updatedJustificatif) {
        Justificatif justificatif = justificatifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificatif non trouvé"));

        justificatif.setNom(updatedJustificatif.getNom());
        justificatif.setCheminFichier(updatedJustificatif.getCheminFichier());

        if (updatedJustificatif.getDemande() != null) {
            Long demandeId = updatedJustificatif.getDemande().getId();
            Demande demande = demandeRepository.findById(demandeId)
                    .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
            justificatif.setDemande(demande);
        }

        Justificatif savedJustificatif = justificatifRepository.save(justificatif);
        return ResponseEntity.ok(savedJustificatif);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        justificatifRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
