package com.example.Gestion_Clinique_Medicale.Repository;

public interface WriteRepository<T> {
    void save(T entity);
    void update(T entity);
    void delete(Long id);
}