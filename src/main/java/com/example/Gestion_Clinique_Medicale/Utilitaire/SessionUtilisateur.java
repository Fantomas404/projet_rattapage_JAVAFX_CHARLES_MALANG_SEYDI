package com.example.Gestion_Clinique_Medicale.Utilitaire;

import com.example.Gestion_Clinique_Medicale.Model.Role;

public class SessionUtilisateur {

    public static Long   id;
    public static String login;
    public static String nomComplet;
    public static Role   role;

    public static boolean estAdmin()          { return role == Role.ADMIN; }
    public static boolean estMedecin()        { return role == Role.MEDECIN; }
    public static boolean estReceptionniste() { return role == Role.RECEPTIONNISTE; }

    public static void fermerSession() {
        id         = null;
        login      = null;
        nomComplet = null;
        role       = null;
    }
}