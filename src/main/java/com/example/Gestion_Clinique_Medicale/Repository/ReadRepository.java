package com.example.Gestion_Clinique_Medicale.Repository;

import java.util.List;

public interface ReadRepository<T> {
    T findById(Long id);
    List<T> findAll();
}