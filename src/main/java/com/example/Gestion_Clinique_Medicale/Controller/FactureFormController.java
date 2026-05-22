package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Consultation;
import com.example.Gestion_Clinique_Medicale.Model.Facture;
import com.example.Gestion_Clinique_Medicale.Service.ConsultationService;
import com.example.Gestion_Clinique_Medicale.Service.FactureService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FactureFormController {

    @FXML private ComboBox<Consultation> consultationCombo;
    @FXML private TextField montantField;
    @FXML private ComboBox<String> modePaiementCombo;
    @FXML private Label errorLabel;

    private final FactureService factureService = new FactureService();
    private final ConsultationService consultationService = new ConsultationService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Runnable onSave;

    @FXML
    public void initialize() {
        List<Consultation> consultations = consultationService.listerToutes();
        consultationCombo.setItems(FXCollections.observableArrayList(consultations));
        consultationCombo.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Consultation c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getRendezVous().getPatient().getNom() + " " +
                            c.getRendezVous().getPatient().getPrenom() +
                            " — " + c.getRendezVous().getDateHeure().format(formatter));
                }
            }
        });
        consultationCombo.setButtonCell(new ListCell<>() {
            protected void updateItem(Consultation c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getRendezVous().getPatient().getNom() + " " +
                            c.getRendezVous().getPatient().getPrenom() +
                            " — " + c.getRendezVous().getDateHeure().format(formatter));
                }
            }
        });

        modePaiementCombo.setItems(FXCollections.observableArrayList(
                "Espèces", "Carte bancaire", "Virement", "Chèque"
        ));
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void enregistrer() {
        Consultation consultation = consultationCombo.getValue();
        String montantTexte = montantField.getText().trim();
        String mode = modePaiementCombo.getValue();

        if (consultation == null || montantTexte.isEmpty() || mode == null) {
            errorLabel.setText("Tous les champs sont obligatoires !");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantTexte);
            if (montant <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errorLabel.setText("Montant invalide ! Entrez un nombre positif.");
            return;
        }

        Facture facture = new Facture();
        facture.setConsultation(consultation);
        facture.setMontantTotal(montant);
        facture.setModePaiement(mode);
        factureService.genererFacture(facture);

        if (onSave != null) onSave.run();
        fermerFenetre();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) montantField.getScene().getWindow();
        stage.close();
    }
}