package com.example.adminapi.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.adminapi.entity.Demande;
import com.example.adminapi.entity.RendezVous;
import com.example.adminapi.repository.DemandeRepository;
import com.example.adminapi.repository.RendezVousRepository;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousRepository rendezVousRepository;
    private final DemandeRepository demandeRepository;

    @PostMapping
    public RendezVous create(@RequestBody RendezVous rendezVous) {
        Long demandeId = rendezVous.getDemande().getId();
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        rendezVous.setDemande(demande);
        return rendezVousRepository.save(rendezVous);
    }

    @GetMapping
    public List<RendezVous> getAll() {
        return rendezVousRepository.findAll();
    }

    @GetMapping("/{id}")
    public RendezVous getById(@PathVariable Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));
    }

    @PutMapping("/{id}")
    public RendezVous update(@PathVariable Long id, @RequestBody RendezVous updatedRdv) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        rdv.setDateHeure(updatedRdv.getDateHeure());
        rdv.setLieu(updatedRdv.getLieu());

        if (updatedRdv.getDemande() != null) {
            Long demandeId = updatedRdv.getDemande().getId();
            Demande demande = demandeRepository.findById(demandeId)
                    .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
            rdv.setDemande(demande);
        }

        return rendezVousRepository.save(rdv);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        rendezVousRepository.deleteById(id);
    }
}
