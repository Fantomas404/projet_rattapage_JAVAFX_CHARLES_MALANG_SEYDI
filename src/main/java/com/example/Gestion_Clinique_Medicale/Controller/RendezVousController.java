package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Service.RendezVousService;
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

public class RendezVousController {

    @FXML private TableView<RendezVous> tableRdv;
    @FXML private TableColumn<RendezVous, String> colId;
    @FXML private TableColumn<RendezVous, String> colPatient;
    @FXML private TableColumn<RendezVous, String> colMedecin;
    @FXML private TableColumn<RendezVous, String> colDate;
    @FXML private TableColumn<RendezVous, String> colStatut;

    private final RendezVousService rdvService = new RendezVousService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPatient().getNom()
                        + " " + c.getValue().getPatient().getPrenom()));
        colMedecin.setCellValueFactory(c ->
                new SimpleStringProperty("Dr. " + c.getValue().getMedecin().getNom()
                        + " " + c.getValue().getMedecin().getPrenom()));
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateHeure().format(formatter)));
        colStatut.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatut().toString()));
        afficherTous();
    }

    @FXML
    private void afficherTous() {
        List<RendezVous> liste = rdvService.listerTous();
        tableRdv.setItems(FXCollections.observableArrayList(liste));
    }

    @FXML
    private void afficherAujourdhui() {
        List<RendezVous> liste = rdvService.listerAujourdhui();
        tableRdv.setItems(FXCollections.observableArrayList(liste));
    }

    @FXML
    private void ouvrirFormulaireAjout() {
        ouvrirFormulaire(null);
    }

    @FXML
    private void modifierRdv() {
        RendezVous selectionne = tableRdv.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un rendez-vous.");
            return;
        }
        ouvrirFormulaire(selectionne);
    }

    @FXML
    private void annulerRdv() {
        RendezVous selectionne = tableRdv.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un rendez-vous.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Annuler le rendez-vous ?");
        confirm.setContentText("Voulez-vous vraiment annuler ce rendez-vous ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                rdvService.annulerRdv(selectionne.getId());
                afficherTous();
            }
        });
    }

    private void ouvrirFormulaire(RendezVous rdv) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/Gestion_Clinique_Medicale/Views/rendezVousForm.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle(rdv == null ? "Nouveau Rendez-vous" : "Modifier Rendez-vous");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            RendezVousFormController form = loader.getController();
            form.setRendezVous(rdv);
            form.setOnSave(this::afficherTous);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erreur formulaire RDV : " + e.getMessage());
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}