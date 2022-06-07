package com.example.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.Preferencia;

@Repository
public interface PreferenciasRepositorio extends JpaRepository<Preferencia, Integer> {
    List<Preferencia> findAll();
    Preferencia findByDescripcion(String role);
    Preferencia findById(int id);
}
