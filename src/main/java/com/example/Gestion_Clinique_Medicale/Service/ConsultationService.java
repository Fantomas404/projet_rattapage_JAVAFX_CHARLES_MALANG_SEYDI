package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Repository.ConsultationRepository;
import com.example.Gestion_Clinique_Medicale.Repository.RendezVousRepository;
import com.example.Gestion_Clinique_Medicale.Model.Consultation;
import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Model.StatutRDV;

import java.util.List;

public class ConsultationService {

    private final ConsultationRepository consultationRepo = new ConsultationRepository();
    private final RendezVousRepository rdvRepo = new RendezVousRepository();

    public void enregistrerConsultation(Consultation consultation) {
        if (consultation.getRendezVous() == null) {
            throw new IllegalArgumentException("Un rendez-vous est obligatoire.");
        }
        consultationRepo.save(consultation);

        RendezVous rdv = consultation.getRendezVous();
        rdv.setStatut(StatutRDV.TERMINE);
        rdvRepo.update(rdv);
    }

    public void modifierConsultation(Consultation consultation) {
        consultationRepo.update(consultation);
    }

    public List<Consultation> listerToutes() {
        return consultationRepo.findAll();
    }

    public Consultation trouverParId(Long id) {
        return consultationRepo.findById(id);
    }
}