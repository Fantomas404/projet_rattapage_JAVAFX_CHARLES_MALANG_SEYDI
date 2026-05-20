package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Medecin;
import com.example.Gestion_Clinique_Medicale.Model.Patient;
import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Service.PatientService;
import com.example.Gestion_Clinique_Medicale.Service.RendezVousService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RendezVousFormController {

    @FXML private ComboBox<Patient> patientCombo;
    @FXML private ComboBox<Medecin> medecinCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> heureCombo;
    @FXML private Label errorLabel;

    private final RendezVousService rdvService = new RendezVousService();
    private final PatientService patientService = new PatientService();

    private RendezVous rendezVous;
    private Runnable onSave;

    @FXML
    public void initialize() {

        patientCombo.setItems(FXCollections.observableArrayList(
                patientService.listerTousLesPatients()));
        patientCombo.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNom() + " " + p.getPrenom());
            }
        });
        patientCombo.setButtonCell(new ListCell<>() {
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNom() + " " + p.getPrenom());
            }
        });

        medecinCombo.setItems(FXCollections.observableArrayList(
                rdvService.listerMedecins()));
        medecinCombo.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : "Dr. " + m.getNom() + " " + m.getPrenom());
            }
        });
        medecinCombo.setButtonCell(new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : "Dr. " + m.getNom() + " " + m.getPrenom());
            }
        });


        for (int h = 8; h <= 18; h++) {
            heureCombo.getItems().add(String.format("%02d:00", h));
            if (h < 18) heureCombo.getItems().add(String.format("%02d:30", h));
        }

        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    public void setRendezVous(RendezVous rdv) {
        this.rendezVous = rdv;
        if (rdv != null) {
            patientCombo.setValue(rdv.getPatient());
            medecinCombo.setValue(rdv.getMedecin());
            datePicker.setValue(rdv.getDateHeure().toLocalDate());
            heureCombo.setValue(String.format("%02d:%02d",
                    rdv.getDateHeure().getHour(),
                    rdv.getDateHeure().getMinute()));
        }
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void enregistrer() {
        Patient patient = patientCombo.getValue();
        Medecin medecin = medecinCombo.getValue();
        LocalDate date = datePicker.getValue();
        String heure = heureCombo.getValue();

        if (patient == null || medecin == null || date == null || heure == null) {
            errorLabel.setText("Tous les champs sont obligatoires !");
            return;
        }

        String[] parts = heure.split(":");
        LocalDateTime dateHeure = LocalDateTime.of(
                date,
                LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]))
        );

        if (rendezVous == null) {
            RendezVous nouveau = new RendezVous();
            nouveau.setPatient(patient);
            nouveau.setMedecin(medecin);
            nouveau.setDateHeure(dateHeure);
            rdvService.planifierRdv(nouveau);
        } else {
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            rendezVous.setDateHeure(dateHeure);
            rdvService.modifierRdv(rendezVous);
        }

        if (onSave != null) onSave.run();
        fermerFenetre();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }
}