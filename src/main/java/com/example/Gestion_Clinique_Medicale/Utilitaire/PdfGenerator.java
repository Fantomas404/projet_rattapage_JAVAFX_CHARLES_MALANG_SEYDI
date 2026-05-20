package com.example.Gestion_Clinique_Medicale.Utilitaire;

import com.example.Gestion_Clinique_Medicale.Model.Consultation;
import com.example.Gestion_Clinique_Medicale.Model.Facture;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    private static final Font TITRE     = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BaseColor.DARK_GRAY);
    private static final Font SOUS_TITRE = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD,  BaseColor.DARK_GRAY);
    private static final Font NORMAL    = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font BOLD      = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font PETIT     = new Font(Font.FontFamily.HELVETICA,  9, Font.ITALIC, BaseColor.GRAY);

    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String genererOrdonnance(Consultation consultation, String outputPath) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            ajouterEntete(document, "ORDONNANCE MEDICALE");
            ajouterSeparateur(document);

            document.add(new Paragraph("INFORMATIONS PATIENT", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Nom complet : " +
                    consultation.getRendezVous().getPatient().getNom() + " " +
                    consultation.getRendezVous().getPatient().getPrenom(), NORMAL));
            document.add(new Paragraph("Telephone : " +
                    consultation.getRendezVous().getPatient().getTelephone(), NORMAL));
            document.add(new Paragraph("Date : " +
                    LocalDate.now().format(DATE_FMT), NORMAL));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            document.add(new Paragraph("MEDECIN TRAITANT", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Dr. " +
                    consultation.getRendezVous().getMedecin().getNom() + " " +
                    consultation.getRendezVous().getMedecin().getPrenom(), NORMAL));
            document.add(new Paragraph("Specialite : " +
                    consultation.getRendezVous().getMedecin().getSpecialite(), NORMAL));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            document.add(new Paragraph("DIAGNOSTIC", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(consultation.getDiagnostic(), NORMAL));
            document.add(Chunk.NEWLINE);

            if (consultation.getObservations() != null
                    && !consultation.getObservations().isEmpty()) {
                document.add(new Paragraph("OBSERVATIONS", SOUS_TITRE));
                document.add(Chunk.NEWLINE);
                document.add(new Paragraph(consultation.getObservations(), NORMAL));
                document.add(Chunk.NEWLINE);
            }

            ajouterSeparateur(document);

            document.add(new Paragraph("PRESCRIPTION", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(consultation.getPrescription(), NORMAL));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            Paragraph signature = new Paragraph("Signature du medecin", PETIT);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            Paragraph nomMedecin = new Paragraph(
                    "Dr. " + consultation.getRendezVous().getMedecin().getNom() +
                            " " + consultation.getRendezVous().getMedecin().getPrenom(), BOLD);
            nomMedecin.setAlignment(Element.ALIGN_RIGHT);
            document.add(nomMedecin);

            ajouterPiedDePage(document);
            document.close();
            return outputPath;

        } catch (Exception e) {
            throw new RuntimeException("Erreur generation ordonnance : " + e.getMessage(), e);
        }
    }

    public static String genererFacturePdf(Facture facture, String outputPath) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            ajouterEntete(document, "FACTURE MEDICALE");
            ajouterSeparateur(document);

            document.add(new Paragraph("N Facture : F-" + facture.getId(), BOLD));
            document.add(new Paragraph("Date : " + facture.getDateFacture().format(DATE_FMT), NORMAL));
            document.add(new Paragraph("Statut : " + facture.getStatut().toString(), BOLD));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            document.add(new Paragraph("PATIENT", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Nom : " +
                    facture.getConsultation().getRendezVous().getPatient().getNom() + " " +
                    facture.getConsultation().getRendezVous().getPatient().getPrenom(), NORMAL));
            document.add(new Paragraph("Telephone : " +
                    facture.getConsultation().getRendezVous().getPatient().getTelephone(), NORMAL));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            document.add(new Paragraph("DETAILS CONSULTATION", SOUS_TITRE));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Medecin : Dr. " +
                    facture.getConsultation().getRendezVous().getMedecin().getNom() + " " +
                    facture.getConsultation().getRendezVous().getMedecin().getPrenom(), NORMAL));
            document.add(new Paragraph("Date RDV : " +
                    facture.getConsultation().getRendezVous().getDateHeure().format(DATETIME_FMT), NORMAL));
            document.add(new Paragraph("Diagnostic : " +
                    facture.getConsultation().getDiagnostic(), NORMAL));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            document.add(new Paragraph("DETAIL DU PAIEMENT", SOUS_TITRE));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1f});
            ajouterCelluleTableau(table, "Prestation", true);
            ajouterCelluleTableau(table, "Montant", true);
            ajouterCelluleTableau(table, "Consultation medicale", false);

            ajouterCelluleTableau(table, facture.getMontantTotal() + " FCFA", false);
            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph total = new Paragraph(
                    "TOTAL : " + facture.getMontantTotal() + " FCFA",
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            Paragraph mode = new Paragraph(
                    "Mode de paiement : " + facture.getModePaiement(), NORMAL);
            mode.setAlignment(Element.ALIGN_RIGHT);
            document.add(mode);

            ajouterPiedDePage(document);
            document.close();
            return outputPath;

        } catch (Exception e) {
            throw new RuntimeException("Erreur generation facture : " + e.getMessage(), e);
        }
    }

    private static void ajouterEntete(Document doc, String titre) throws DocumentException {
        Paragraph clinique = new Paragraph("CLINIQUE MEDICALE", TITRE);
        clinique.setAlignment(Element.ALIGN_CENTER);
        doc.add(clinique);

        Paragraph adresse = new Paragraph("123 Rue de la Sante - Tel: 00 00 00 00", PETIT);
        adresse.setAlignment(Element.ALIGN_CENTER);
        doc.add(adresse);

        doc.add(Chunk.NEWLINE);

        Paragraph titrePara = new Paragraph(titre, SOUS_TITRE);
        titrePara.setAlignment(Element.ALIGN_CENTER);
        doc.add(titrePara);

        doc.add(Chunk.NEWLINE);
    }

    private static void ajouterSeparateur(Document doc) throws DocumentException {
        PdfPTable ligne = new PdfPTable(1);
        ligne.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthBottom(1f);
        cell.setBorderColorBottom(BaseColor.LIGHT_GRAY);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setPadding(3f);
        ligne.addCell(cell);
        doc.add(ligne);
        doc.add(Chunk.NEWLINE);
    }

    private static void ajouterPiedDePage(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        ajouterSeparateur(doc);
        Paragraph pied = new Paragraph(
                "Document genere le " + LocalDate.now().format(DATE_FMT) +
                        " - Clinique Medicale", PETIT);
        pied.setAlignment(Element.ALIGN_CENTER);
        doc.add(pied);
    }

    private static void ajouterCelluleTableau(PdfPTable table, String texte, boolean header) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, header ? BOLD : NORMAL));
        cell.setPadding(8);
        if (header) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        table.addCell(cell);
    }
}