package com.example.Gestion_Clinique_Medicale.Repository;

import com.example.Gestion_Clinique_Medicale.Model.Utilisateur;
import com.example.Gestion_Clinique_Medicale.config.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UtilisateurRepository extends BaseRepositoryImpl<Utilisateur> {

    public UtilisateurRepository() { super(Utilisateur.class); }

    public Utilisateur findByLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.login = :login", Utilisateur.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}