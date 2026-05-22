package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Consultation;
import com.example.Gestion_Clinique_Medicale.Service.ConsultationService;
import com.example.Gestion_Clinique_Medicale.Utilitaire.PdfGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultationController {

    @FXML private TableView<Consultation> tableConsultations;
    @FXML private TableColumn<Consultation, String> colId;
    @FXML private TableColumn<Consultation, String> colPatient;
    @FXML private TableColumn<Consultation, String> colMedecin;
    @FXML private TableColumn<Consultation, String> colDiagnostic;
    @FXML private TableColumn<Consultation, String> colDate;

    private final ConsultationService consultationService = new ConsultationService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getRendezVous().getPatient().getNom() + " " +
                                c.getValue().getRendezVous().getPatient().getPrenom()));
        colMedecin.setCellValueFactory(c ->
                new SimpleStringProperty(
                        "Dr. " + c.getValue().getRendezVous().getMedecin().getNom() + " " +
                                c.getValue().getRendezVous().getMedecin().getPrenom()));
        colDiagnostic.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDiagnostic()));
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getRendezVous().getDateHeure().format(formatter)));
        chargerConsultations();
    }

    private void chargerConsultations() {
        List<Consultation> liste = consultationService.listerToutes();
        tableConsultations.setItems(FXCollections.observableArrayList(liste));
    }

    @FXML private void ouvrirFormulaireAjout() { ouvrirFormulaire(null); }

    @FXML
    private void modifierConsultation() {
        Consultation selectionne = tableConsultations.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une consultation.");
            return;
        }
        ouvrirFormulaire(selectionne);
    }

    @FXML
    private void genererOrdonnancePdf() {
        Consultation selectionne = tableConsultations.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une consultation.");
            return;
        }
        String path = System.getProperty("user.home") + "/Desktop/ordonnance_"
                + selectionne.getId() + ".pdf";
        try {
            PdfGenerator.genererOrdonnance(selectionne, path);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF généré");
            alert.setContentText("Ordonnance sauvegardée sur le Bureau :\n" + path);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur génération PDF : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void ouvrirFormulaire(Consultation consultation) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/Gestion_Clinique_Medicale/Views/consultationForm.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle(consultation == null ? "Nouvelle Consultation" : "Modifier Consultation");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            ConsultationFormController form = loader.getController();
            form.setConsultation(consultation);
            form.setOnSave(this::chargerConsultations);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erreur formulaire consultation : " + e.getMessage());
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}