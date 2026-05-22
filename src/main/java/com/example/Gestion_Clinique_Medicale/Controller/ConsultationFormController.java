package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Consultation;
import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.Model.StatutRDV;
import com.example.Gestion_Clinique_Medicale.Service.ConsultationService;
import com.example.Gestion_Clinique_Medicale.Service.RendezVousService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationFormController {

    @FXML private ComboBox<RendezVous> rdvCombo;
    @FXML private TextField medecinField;
    @FXML private TextArea diagnosticArea;
    @FXML private TextArea observationsArea;
    @FXML private TextArea prescriptionArea;
    @FXML private Label errorLabel;

    private final ConsultationService consultationService = new ConsultationService();
    private final RendezVousService rdvService = new RendezVousService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Consultation consultation;
    private Runnable onSave;

    @FXML
    public void initialize() {
        List<RendezVous> rdvProgrammes = rdvService.listerTous().stream()
                .filter(r -> r.getStatut() == StatutRDV.PROGRAMME)
                .collect(Collectors.toList());

        rdvCombo.setItems(FXCollections.observableArrayList(rdvProgrammes));

        rdvCombo.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(RendezVous r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) setText(null);
                else setText(r.getPatient().getNom() + " " + r.getPatient().getPrenom()
                        + " — " + r.getDateHeure().format(formatter));
            }
        });

        rdvCombo.setButtonCell(new ListCell<>() {
            protected void updateItem(RendezVous r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) setText(null);
                else setText(r.getPatient().getNom() + " " + r.getPatient().getPrenom()
                        + " — " + r.getDateHeure().format(formatter));
            }
        });
    }

    @FXML
    private void onRdvSelectionne() {
        RendezVous rdv = rdvCombo.getValue();
        if (rdv != null) {
            medecinField.setText("Dr. " + rdv.getMedecin().getPrenom()
                    + " " + rdv.getMedecin().getNom());
        } else {
            medecinField.clear();
        }
    }

    public void setConsultation(Consultation c) {
        this.consultation = c;
        if (c != null) {
            rdvCombo.setValue(c.getRendezVous());
            rdvCombo.setDisable(true);

            medecinField.setText("Dr. " + c.getRendezVous().getMedecin().getPrenom()
                    + " " + c.getRendezVous().getMedecin().getNom());
            diagnosticArea.setText(c.getDiagnostic());
            observationsArea.setText(c.getObservations());
            prescriptionArea.setText(c.getPrescription());
        }
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void enregistrer() {
        RendezVous rdv = rdvCombo.getValue();
        String diagnostic = diagnosticArea.getText().trim();

        if (rdv == null || diagnostic.isEmpty()) {
            errorLabel.setText("Le rendez-vous et le diagnostic sont obligatoires !");
            return;
        }

        if (consultation == null) {
            Consultation nouvelle = new Consultation();
            nouvelle.setRendezVous(rdv);
            nouvelle.setDiagnostic(diagnostic);
            nouvelle.setObservations(observationsArea.getText().trim());
            nouvelle.setPrescription(prescriptionArea.getText().trim());
            consultationService.enregistrerConsultation(nouvelle);
        } else {
            consultation.setDiagnostic(diagnostic);
            consultation.setObservations(observationsArea.getText().trim());
            consultation.setPrescription(prescriptionArea.getText().trim());
            consultationService.modifierConsultation(consultation);
        }

        if (onSave != null) onSave.run();
        fermerFenetre();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) diagnosticArea.getScene().getWindow();
        stage.close();
    }
}