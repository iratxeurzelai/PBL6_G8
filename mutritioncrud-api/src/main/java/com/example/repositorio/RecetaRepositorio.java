package com.example.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.Receta;

@Repository
public interface RecetaRepositorio extends JpaRepository<Receta, Long>{
    Receta findByTitle(String title);
    Receta findById(int id);
}