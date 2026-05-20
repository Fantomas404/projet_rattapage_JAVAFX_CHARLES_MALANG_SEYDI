package com.example.Gestion_Clinique_Medicale.Controller;

import com.example.Gestion_Clinique_Medicale.Model.Role;
import com.example.Gestion_Clinique_Medicale.Model.Utilisateur;
import com.example.Gestion_Clinique_Medicale.Repository.UtilisateurRepository;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SecurityUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class GestionUtilisateursController {

    @FXML private TableView<Utilisateur> tableUtilisateurs;
    @FXML private TableColumn<Utilisateur, String> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colLogin;
    @FXML private TableColumn<Utilisateur, String> colRole;

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboRole;
    @FXML private Label lblMessage;

    private final UtilisateurRepository repo = new UtilisateurRepository();
    private Utilisateur utilisateurSelectionne = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colNom.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNom()));
        colPrenom.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPrenom()));
        colLogin.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getLogin()));
        colRole.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRole().toString()));

        comboRole.setItems(FXCollections.observableArrayList("MEDECIN", "RECEPTIONNISTE"));
        comboRole.setValue("MEDECIN");

        tableUtilisateurs.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        utilisateurSelectionne = selected;
                        txtNom.setText(selected.getNom());
                        txtPrenom.setText(selected.getPrenom());
                        txtLogin.setText(selected.getLogin());
                        txtPassword.clear();
                        comboRole.setValue(selected.getRole().toString());
                    }
                });

        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> tous = repo.findAll();

        List<Utilisateur> filtres = tous.stream()
                .filter(u -> u.getRole() == Role.MEDECIN
                        || u.getRole() == Role.RECEPTIONNISTE)
                .collect(Collectors.toList());
        tableUtilisateurs.setItems(FXCollections.observableArrayList(filtres));
    }

    @FXML
    private void enregistrer() {
        String nom      = txtNom.getText().trim();
        String prenom   = txtPrenom.getText().trim();
        String login    = txtLogin.getText().trim();
        String password = txtPassword.getText();
        String roleStr  = comboRole.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || login.isEmpty()) {
            afficherMessage("Nom, prenom et login sont obligatoires.", false);
            return;
        }

        Role role = roleStr.equals("MEDECIN") ? Role.MEDECIN : Role.RECEPTIONNISTE;

        if (utilisateurSelectionne == null) {

            if (password.isEmpty()) {
                afficherMessage("Le mot de passe est obligatoire.", false);
                return;
            }
            if (repo.findByLogin(login) != null) {
                afficherMessage("Ce login existe deja.", false);
                return;
            }
            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setLogin(login);
            u.setPassword(SecurityUtil.hashPassword(password));
            u.setRole(role);
            repo.save(u);
            afficherMessage("Utilisateur cree avec succes !", true);
        } else {

            utilisateurSelectionne.setNom(nom);
            utilisateurSelectionne.setPrenom(prenom);
            utilisateurSelectionne.setLogin(login);
            utilisateurSelectionne.setRole(role);
            if (!password.isEmpty()) {
                utilisateurSelectionne.setPassword(SecurityUtil.hashPassword(password));
            }
            repo.update(utilisateurSelectionne);
            afficherMessage("Utilisateur modifie avec succes !", true);
        }

        viderFormulaire();
        chargerUtilisateurs();
    }

    @FXML
    private void supprimer() {
        Utilisateur selectionne = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            afficherMessage("Selectionnez un utilisateur.", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer l'utilisateur ?");
        confirm.setContentText("Supprimer " + selectionne.getNom()
                + " " + selectionne.getPrenom() + " ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                repo.delete(selectionne.getId());
                viderFormulaire();
                chargerUtilisateurs();
                afficherMessage("Utilisateur supprime.", true);
            }
        });
    }

    @FXML
    private void nouveau() {
        viderFormulaire();
        afficherMessage("", true);
    }

    private void viderFormulaire() {
        txtNom.clear();
        txtPrenom.clear();
        txtLogin.clear();
        txtPassword.clear();
        comboRole.setValue("MEDECIN");
        utilisateurSelectionne = null;
        tableUtilisateurs.getSelectionModel().clearSelection();
    }

    private void afficherMessage(String msg, boolean succes) {
        lblMessage.setText(msg);
        lblMessage.setStyle(succes
                ? "-fx-text-fill: #276749; -fx-font-size: 12px;"
                : "-fx-text-fill: #c53030; -fx-font-size: 12px;");
    }
}