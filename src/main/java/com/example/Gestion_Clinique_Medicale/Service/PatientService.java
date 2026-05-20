package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Repository.PatientRepository;
import com.example.Gestion_Clinique_Medicale.Model.Patient;
import java.util.List;

public class PatientService {

    private final PatientRepository patientRepo = new PatientRepository();

    public void enregistrerPatient(Patient patient) {
        if (patient.getNom() == null || patient.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du patient est obligatoire.");
        }
        patientRepo.save(patient);
    }

    public List<Patient> listerTousLesPatients() {
        return patientRepo.findAll();
    }

    public void supprimerPatient(Long id) {
        patientRepo.delete(id);
    }

    public Patient trouverParId(Long id) {
        return patientRepo.findById(id);
    }

    public void modifierPatient(Patient patient) {
        patientRepo.update(patient);
    }
}