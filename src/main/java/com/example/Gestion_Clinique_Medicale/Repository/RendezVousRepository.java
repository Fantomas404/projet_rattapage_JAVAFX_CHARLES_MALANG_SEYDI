package com.example.Gestion_Clinique_Medicale.Repository;

import com.example.Gestion_Clinique_Medicale.Model.RendezVous;
import com.example.Gestion_Clinique_Medicale.config.JPAUtil;
import javax.persistence.EntityManager;
import java.util.List;

public class RendezVousRepository extends BaseRepositoryImpl<RendezVous> {

    public RendezVousRepository() { super(RendezVous.class); }

    public List<RendezVous> findByMedecin(Long medecinId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId", RendezVous.class)
                    .setParameter("medecinId", medecinId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<RendezVous> findToday() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM RendezVous r WHERE CAST(r.dateHeure AS date) = CURRENT_DATE", RendezVous.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}