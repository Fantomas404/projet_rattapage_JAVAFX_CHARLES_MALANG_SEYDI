package com.example.Gestion_Clinique_Medicale;

import com.example.Gestion_Clinique_Medicale.Repository.UtilisateurRepository;
import com.example.Gestion_Clinique_Medicale.Model.Role;
import com.example.Gestion_Clinique_Medicale.Model.Utilisateur;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SecurityUtil;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            UtilisateurRepository repo = new UtilisateurRepository();
            if (repo.findByLogin("admin") == null) {
                Utilisateur u = new Utilisateur();
                u.setNom("M."); u.setPrenom("Longate");
                u.setLogin("admin");
                u.setPassword(SecurityUtil.hashPassword("admin123"));
                u.setRole(Role.ADMIN);
                repo.save(u);
            }
            if (repo.findByLogin("medecin") == null) {
                Utilisateur u = new Utilisateur();
                u.setNom("Ba "); u.setPrenom(" Cheikh Tidiane ");
                u.setLogin("medecin");
                u.setPassword(SecurityUtil.hashPassword("medecin123"));
                u.setRole(Role.MEDECIN);
                repo.save(u);
            }
            if (repo.findByLogin("reception") == null) {
                Utilisateur u = new Utilisateur();
                u.setNom("Diallo"); u.setPrenom("Fatou");
                u.setLogin("reception");
                u.setPassword(SecurityUtil.hashPassword("reception123"));
                u.setRole(Role.RECEPTIONNISTE);
                repo.save(u);
            }
        } catch (Exception e) {
            System.err.println("Erreur DB : " + e.getMessage());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource(
                        "/com/example/Gestion_Clinique_Medicale/Views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Clinique Medicale - Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}