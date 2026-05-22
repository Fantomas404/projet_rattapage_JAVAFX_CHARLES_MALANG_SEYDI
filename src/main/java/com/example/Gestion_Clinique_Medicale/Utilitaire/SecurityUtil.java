package com.example.Gestion_Clinique_Medicale.Utilitaire;

import java.security.MessageDigest;
import java.util.Base64;

public class SecurityUtil {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }

    public static class Pdfgenerator {
    }
}