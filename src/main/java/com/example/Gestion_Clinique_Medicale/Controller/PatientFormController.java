package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Patient;
import com.example.Gestion_Clinique_Medicale.Service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PatientFormController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField telephoneField;
    @FXML private Label errorLabel;

    private final PatientService patientService = new PatientService();
    private Patient patient;
    private Runnable onSave;

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient != null) {
            nomField.setText(patient.getNom());
            prenomField.setText(patient.getPrenom());
            telephoneField.setText(patient.getTelephone());
        }
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void enregistrer() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            errorLabel.setText("Le nom et prénom sont obligatoires !");
            return;
        }

        if (patient == null) {
            // Nouveau patient
            Patient nouveau = new Patient();
            nouveau.setNom(nom);
            nouveau.setPrenom(prenom);
            nouveau.setTelephone(telephone);
            patientService.enregistrerPatient(nouveau);
        } else {
            // Modification
            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setTelephone(telephone);
            patientService.modifierPatient(patient);
        }

        if (onSave != null) onSave.run();
        fermerFenetre();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}