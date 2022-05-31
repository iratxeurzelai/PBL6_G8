package com.example.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.RecetaSemana;

@Repository
public interface RecetaSemanaRepositorio extends JpaRepository<RecetaSemana, Long>{
    RecetaSemana save(RecetaSemana recetaSemana);
    List<RecetaSemana> findAll();
    List<RecetaSemana> findByUserId(int userId);
}
