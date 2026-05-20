package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Repository.MedecinRepository;
import com.example.Gestion_Clinique_Medicale.Repository.RendezVousRepository;
import com.example.Gestion_Clinique_Medicale.Model.Medecin;
import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Model.StatutRDV;

import java.util.List;

public class RendezVousService {

    private final RendezVousRepository rdvRepo = new RendezVousRepository();
    private final MedecinRepository medecinRepo = new MedecinRepository();

    public void planifierRdv(RendezVous rdv) {
        if (rdv.getPatient() == null || rdv.getMedecin() == null || rdv.getDateHeure() == null) {
            throw new IllegalArgumentException("Patient, médecin et date sont obligatoires.");
        }
        rdv.setStatut(StatutRDV.PROGRAMME);
        rdvRepo.save(rdv);
    }

    public void modifierRdv(RendezVous rdv) {
        rdvRepo.update(rdv);
    }

    public void annulerRdv(Long id) {
        RendezVous rdv = rdvRepo.findById(id);
        if (rdv != null) {
            rdv.setStatut(StatutRDV.ANNULE);
            rdvRepo.update(rdv);
        }
    }

    public List<RendezVous> listerTous() {
        return rdvRepo.findAll();
    }

    public List<RendezVous> listerAujourdhui() {
        return rdvRepo.findToday();
    }

    public List<Medecin> listerMedecins() {
        return medecinRepo.findAll();
    }
}