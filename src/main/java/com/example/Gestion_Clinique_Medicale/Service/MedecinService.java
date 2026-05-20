package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Repository.MedecinRepository;
import com.example.Gestion_Clinique_Medicale.Model.Medecin;

import java.util.List;

public class MedecinService {

    private final MedecinRepository medecinRepo = new MedecinRepository();

    public void ajouterMedecin(Medecin medecin) {
        medecinRepo.save(medecin);
    }

    public List<Medecin> listerTous() {
        return medecinRepo.findAll();
    }
}