package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Utilitaire.SessionUtilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MedecinDashboardController {

    @FXML private StackPane contenuPrincipal;
    @FXML private Label labelUtilisateur;
    @FXML private Label labelRole;
    @FXML private Label lblTitrePage;
    @FXML private HBox btnRendezVous;
    @FXML private HBox btnConsultations;

    private static final String ACTIF   =
            "-fx-background-color: #dbeafe; -fx-cursor: hand; -fx-padding: 0 14 0 14;";
    private static final String INACTIF =
            "-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 14 0 14;";
    private static final String TEXTE_ACTIF   =
            "-fx-text-fill: #1d4ed8; -fx-font-size: 13px; -fx-font-weight: bold;";
    private static final String TEXTE_INACTIF =
            "-fx-text-fill: #94a3b8; -fx-font-size: 13px;";

    @FXML
    public void initialize() {
        labelUtilisateur.setText("Bienvenue, Dr. " + SessionUtilisateur.nomComplet);
        labelRole.setText("MEDECIN");
        ouvrirRendezVous();
    }

    @FXML private void ouvrirRendezVous() {
        setActif(btnRendezVous, "Mes Rendez-vous");
        chargerVue("rendezVous.fxml");
    }

    @FXML private void ouvrirConsultations() {
        setActif(btnConsultations, "Mes Consultations");
        chargerVue("consultations.fxml");
    }

    @FXML private void seDeconnecter() {
        try {
            SessionUtilisateur.fermerSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/Gestion_Clinique_Medicale/Views/login.fxml"));
            Stage stage = (Stage) contenuPrincipal.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Clinique Medicale - Connexion");
            stage.setMaximized(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur deconnexion : " + e.getMessage());
        }
    }

    private void setActif(HBox bouton, String titre) {
        if (lblTitrePage != null) lblTitrePage.setText(titre);
        for (HBox btn : new HBox[]{btnRendezVous, btnConsultations}) {
            if (btn == null) continue;
            btn.setStyle(INACTIF);
            if (btn.getChildren().size() >= 2) {
                javafx.scene.layout.VBox vbox =
                        (javafx.scene.layout.VBox) btn.getChildren().get(1);
                if (!vbox.getChildren().isEmpty())
                    ((Label) vbox.getChildren().get(0)).setStyle(TEXTE_INACTIF);
            }
        }
        bouton.setStyle(ACTIF);
        if (bouton.getChildren().size() >= 2) {
            javafx.scene.layout.VBox vbox =
                    (javafx.scene.layout.VBox) bouton.getChildren().get(1);
            if (!vbox.getChildren().isEmpty())
                ((Label) vbox.getChildren().get(0)).setStyle(TEXTE_ACTIF);
        }
    }

    private void chargerVue(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/Gestion_Clinique_Medicale/Views/" + fxmlFile));
            contenuPrincipal.getChildren().clear();
            contenuPrincipal.getChildren().add(loader.load());
        } catch (IOException e) {
            System.err.println("Erreur chargement : " + fxmlFile + " - " + e.getMessage());
        }
    }
}