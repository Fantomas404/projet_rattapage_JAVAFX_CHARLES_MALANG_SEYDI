package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Repository.FactureRepository;
import com.example.Gestion_Clinique_Medicale.Model.Facture;
import com.example.Gestion_Clinique_Medicale.Model.StatutPaiement;

import java.time.LocalDate;
import java.util.List;

public class FactureService {

    private final FactureRepository factureRepo = new FactureRepository();

    public void genererFacture(Facture facture) {
        if (facture.getConsultation() == null) {
            throw new IllegalArgumentException("Une consultation est obligatoire.");
        }
        if (facture.getMontantTotal() == null || facture.getMontantTotal() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }
        facture.setDateFacture(LocalDate.now());
        facture.setStatut(StatutPaiement.NON_PAYE);
        factureRepo.save(facture);
    }

    public void marquerPaye(Long id) {
        Facture facture = factureRepo.findById(id);
        if (facture != null) {
            facture.setStatut(StatutPaiement.PAYE);
            factureRepo.update(facture);
        }
    }

    public List<Facture> listerToutes() {
        return factureRepo.findAll();
    }

    public Facture trouverParId(Long id) {
        return factureRepo.findById(id);
    }
}