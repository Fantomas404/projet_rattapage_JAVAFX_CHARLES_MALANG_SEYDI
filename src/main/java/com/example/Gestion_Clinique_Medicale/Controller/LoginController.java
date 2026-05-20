package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Role;
import com.example.Gestion_Clinique_Medicale.Service.AuthService;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SessionUtilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String login    = loginField.getText().trim();
        String password = passwordField.getText();
        errorLabel.setVisible(false);

        if (login.isEmpty() || password.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        if (authService.login(login, password)) {

            Role role = SessionUtilisateur.role;
            System.out.println("Role detecte : " + role);

            String fxml  = obtenirFxml(role);
            String titre = obtenirTitre(role);

            System.out.println("FXML cible : " + fxml);

            URL fxmlUrl = getClass().getResource(
                    "/com/example/Gestion_Clinique_Medicale/Views/" + fxml);

            System.out.println("URL resolue : " + fxmlUrl);

            if (fxmlUrl == null) {
                afficherErreur("Erreur : fichier " + fxml + " introuvable !");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle(titre);
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                afficherErreur("Erreur chargement : " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            afficherErreur("Identifiants incorrects. Acces refuse.");
            passwordField.clear();
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private String obtenirFxml(Role role) {
        if (role == null) return "dashboard.fxml";
        switch (role) {
            case MEDECIN:        return "medecinDashboard.fxml";
            case RECEPTIONNISTE: return "receptionDashboard.fxml";
            case ADMIN:
            default:             return "dashboard.fxml";
        }
    }

    private String obtenirTitre(Role role) {
        if (role == null) return "Clinique Medicale";
        switch (role) {
            case MEDECIN:        return "Clinique Medicale - Espace Medecin";
            case RECEPTIONNISTE: return "Clinique Medicale - Espace Reception";
            case ADMIN:
            default:             return "Clinique Medicale - Dashboard Admin";
        }
    }
}