package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Model.Utilisateur;
import com.example.Gestion_Clinique_Medicale.Model.Role;
import com.example.Gestion_Clinique_Medicale.Repository.UtilisateurRepository;
import com.example.Gestion_Clinique_Medicale.Service.ConsultationService;
import com.example.Gestion_Clinique_Medicale.Service.FactureService;
import com.example.Gestion_Clinique_Medicale.Service.PatientService;
import com.example.Gestion_Clinique_Medicale.Service.RendezVousService;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SessionUtilisateur;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AccueilController {

    @FXML private Label lblBienvenue;
    @FXML private Label lblDate;
    @FXML private Label lblNbPatients;
    @FXML private Label lblNbRdv;
    @FXML private Label lblNbConsultations;
    @FXML private Label lblNbFactures;

    @FXML private TableView<RendezVous> tableRdvJour;
    @FXML private TableColumn<RendezVous, String> colRdvPatient;
    @FXML private TableColumn<RendezVous, String> colRdvMedecin;
    @FXML private TableColumn<RendezVous, String> colRdvHeure;
    @FXML private TableColumn<RendezVous, String> colRdvStatut;

    @FXML private ListView<String> listeMedecins;

    private final PatientService       patientService       = new PatientService();
    private final RendezVousService    rdvService           = new RendezVousService();
    private final ConsultationService  consultationService  = new ConsultationService();
    private final FactureService       factureService       = new FactureService();
    private final UtilisateurRepository utilisateurRepo     = new UtilisateurRepository();

    private final DateTimeFormatter heureFormatter =
            DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy",
                    java.util.Locale.FRENCH);

    @FXML
    public void initialize() {
        lblBienvenue.setText("Bonjour, " + SessionUtilisateur.nomComplet);
        lblDate.setText(LocalDateTime.now().format(dateFormatter));

        lblNbPatients.setText(
                String.valueOf(patientService.listerTousLesPatients().size()));
        lblNbRdv.setText(
                String.valueOf(rdvService.listerAujourdhui().size()));
        lblNbConsultations.setText(
                String.valueOf(consultationService.listerToutes().size()));

        long nonPayees = factureService.listerToutes().stream()
                .filter(f -> f.getStatut().toString().equals("NON_PAYE"))
                .count();
        lblNbFactures.setText(String.valueOf(nonPayees));

        colRdvPatient.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getPatient().getNom() + " "
                                + c.getValue().getPatient().getPrenom()));
        colRdvMedecin.setCellValueFactory(c ->
                new SimpleStringProperty(
                        "Dr. " + c.getValue().getMedecin().getNom()));
        colRdvHeure.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDateHeure().format(heureFormatter)));
        colRdvStatut.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getStatut().toString()));

        tableRdvJour.setItems(FXCollections.observableArrayList(
                rdvService.listerAujourdhui()));

        List<Utilisateur> medecins = utilisateurRepo.findAll().stream()
                .filter(u -> u.getRole() == Role.MEDECIN)
                .collect(Collectors.toList());

        List<String> noms = medecins.stream()
                .map(u -> "Dr. " + u.getNom() + " " + u.getPrenom())
                .collect(Collectors.toList());

        listeMedecins.setItems(FXCollections.observableArrayList(noms));
    }
}