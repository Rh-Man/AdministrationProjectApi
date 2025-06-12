package com.example.adminapi.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.Justificatif;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.JustificatifRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    // Methode qui permet d'upload les documents justificatif
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("demandeId") Long demandeId) {

        try {
            Demande demande = demandeRepository.findById(demandeId)
                    .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

            // Créer le dossier uploads s’il n’existe pas
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Générer un nom de fichier unique
            String nomUnique = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + nomUnique);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Créer et enregistrer le justificatif
            Justificatif justificatif = new Justificatif();
            justificatif.setNom(file.getOriginalFilename());
            justificatif.setCheminFichier(path.toString());
            justificatif.setDemande(demande);

            justificatifRepository.save(justificatif);

            return ResponseEntity.ok("Fichier uploadé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l’upload : " + e.getMessage());
        }
    }

}
