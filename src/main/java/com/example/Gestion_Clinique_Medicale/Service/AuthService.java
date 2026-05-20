package com.example.Gestion_Clinique_Medicale.Service;

import com.example.Gestion_Clinique_Medicale.Model.Utilisateur;
import com.example.Gestion_Clinique_Medicale.Repository.UtilisateurRepository;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SecurityUtil;
import com.example.Gestion_Clinique_Medicale.Utilitaire.SessionUtilisateur;

public class AuthService {

    private final UtilisateurRepository repo = new UtilisateurRepository();

    public boolean login(String login, String password) {
        Utilisateur utilisateur = repo.findByLogin(login);

        if (utilisateur == null) {
            System.out.println("Utilisateur introuvable : " + login);
            return false;
        }

        String passwordHashe = SecurityUtil.hashPassword(password);
        boolean motDePasseOk = passwordHashe.equals(utilisateur.getPassword());

        if (!motDePasseOk) {
            System.out.println("Mot de passe incorrect pour : " + login);
            return false;
        }

        SessionUtilisateur.id         = utilisateur.getId();
        SessionUtilisateur.login      = utilisateur.getLogin();
        SessionUtilisateur.nomComplet = utilisateur.getPrenom() + " " + utilisateur.getNom();
        SessionUtilisateur.role       = utilisateur.getRole();

        System.out.println("Connexion reussie : " + login + " | Role : " + utilisateur.getRole());
        return true;
    }
}