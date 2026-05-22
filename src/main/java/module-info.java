module com.example.Gestion_Clinique_Medicale {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires org.controlsfx.controls;
    requires itextpdf;

    opens com.example.Gestion_Clinique_Medicale to javafx.fxml;
    opens com.example.Gestion_Clinique_Medicale.Controller to javafx.fxml;
    opens com.example.Gestion_Clinique_Medicale.Model to org.hibernate.orm.core;

    exports com.example.Gestion_Clinique_Medicale;
    exports com.example.Gestion_Clinique_Medicale.Controller;
    exports com.example.Gestion_Clinique_Medicale.Model;
    exports com.example.Gestion_Clinique_Medicale.Service;
    exports com.example.Gestion_Clinique_Medicale.Repository;
    exports com.example.Gestion_Clinique_Medicale.Utilitaire;
}