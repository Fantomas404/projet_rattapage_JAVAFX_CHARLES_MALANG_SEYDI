package com.example.Gestion_Clinique_Medicale.Model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montantTotal;
    private LocalDate dateFacture;
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    public Facture() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
    public LocalDate getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public StatutPaiement getStatut() { return statut; }
    public void setStatut(StatutPaiement statut) { this.statut = statut; }
    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }
}