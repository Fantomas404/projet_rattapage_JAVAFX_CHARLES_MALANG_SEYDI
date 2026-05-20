package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Patient;
import com.example.Gestion_Clinique_Medicale.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PatientController {

    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, Long>   colId;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TextField searchField;

    private final PatientService patientService = new PatientService();
    private List<Patient> tousLesPatients;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        chargerPatients();
    }

    private void chargerPatients() {
        tousLesPatients = patientService.listerTousLesPatients();
        tablePatients.setItems(FXCollections.observableArrayList(tousLesPatients));
    }

    @FXML
    private void rechercherPatient() {
        String recherche = searchField.getText().toLowerCase().trim();
        if (recherche.isEmpty()) {
            chargerPatients();
            return;
        }
        List<Patient> filtres = tousLesPatients.stream()
                .filter(p -> p.getNom().toLowerCase().contains(recherche)
                        || p.getPrenom().toLowerCase().contains(recherche)
                        || (p.getTelephone() != null && p.getTelephone().contains(recherche)))
                .collect(Collectors.toList());
        tablePatients.setItems(FXCollections.observableArrayList(filtres));
    }

    @FXML
    private void ouvrirFormulaireAjout() {
        ouvrirFormulaire(null);
    }

    @FXML
    private void modifierPatient() {
        Patient selectionne = tablePatients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un patient à modifier.");
            return;
        }
        ouvrirFormulaire(selectionne);
    }

    @FXML
    private void supprimerPatient() {
        Patient selectionne = tablePatients.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner un patient à supprimer.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le patient ?");
        confirm.setContentText("Voulez-vous vraiment supprimer "
                + selectionne.getNom() + " " + selectionne.getPrenom() + " ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                patientService.supprimerPatient(selectionne.getId());
                chargerPatients();
            }
        });
    }

    private void ouvrirFormulaire(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/Gestion_Clinique_Medicale/Views/patientForm.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle(patient == null ? "Nouveau Patient" : "Modifier Patient");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            PatientFormController formController = loader.getController();
            formController.setPatient(patient);
            formController.setOnSave(this::chargerPatients);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erreur ouverture formulaire : " + e.getMessage());
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}