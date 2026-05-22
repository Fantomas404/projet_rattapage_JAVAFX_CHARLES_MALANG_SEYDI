package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Facture;
import com.example.Gestion_Clinique_Medicale.Service.FactureService;
import com.example.Gestion_Clinique_Medicale.Utilitaire.PdfGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FacturationController {

    @FXML private TableView<Facture> tableFactures;
    @FXML private TableColumn<Facture, String> colId;
    @FXML private TableColumn<Facture, String> colPatient;
    @FXML private TableColumn<Facture, String> colMontant;
    @FXML private TableColumn<Facture, String> colMode;
    @FXML private TableColumn<Facture, String> colDate;
    @FXML private TableColumn<Facture, String> colStatut;

    private final FactureService factureService = new FactureService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colPatient.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getConsultation().getRendezVous().getPatient().getNom() + " " +
                                c.getValue().getConsultation().getRendezVous().getPatient().getPrenom()));
        colMontant.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMontantTotal() + " FCFA"));
        colMode.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getModePaiement()));
        colDate.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateFacture().toString()));
        colStatut.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatut().toString()));

        tableFactures.setRowFactory(tv -> new TableRow<>() {
            protected void updateItem(Facture f, boolean empty) {
                super.updateItem(f, empty);
                if (f == null || empty) setStyle("");
                else if (f.getStatut().toString().equals("PAYE"))
                    setStyle("-fx-background-color: #d5f5e3;");
                else setStyle("-fx-background-color: #fdecea;");
            }
        });
        chargerFactures();
    }

    private void chargerFactures() {
        List<Facture> liste = factureService.listerToutes();
        tableFactures.setItems(FXCollections.observableArrayList(liste));
    }

    @FXML
    private void ouvrirFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/Gestion_Clinique_Medicale/Views/factureForm.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle("Nouvelle Facture");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            FactureFormController form = loader.getController();
            form.setOnSave(this::chargerFactures);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Erreur formulaire facture : " + e.getMessage());
        }
    }

    @FXML
    private void marquerPaye() {
        Facture selectionne = tableFactures.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une facture.");
            return;
        }
        factureService.marquerPaye(selectionne.getId());
        chargerFactures();
    }

    @FXML
    private void genererFacturePdf() {
        Facture selectionne = tableFactures.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une facture.");
            return;
        }
        File dossierFactures = new File(System.getProperty("user.dir") + File.separator + "factures");
        if (!dossierFactures.exists() && !dossierFactures.mkdirs()) {
            afficherAlerte("Erreur", "Impossible de créer le dossier factures.");
            return;
        }
        String path = dossierFactures.getAbsolutePath()
                + File.separator + "facture_" + selectionne.getId() + ".pdf";
        try {
            PdfGenerator.genererFacturePdf(selectionne, path);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF généré");
            alert.setContentText("Facture sauvegardée dans :\n" + path);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur génération PDF : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}