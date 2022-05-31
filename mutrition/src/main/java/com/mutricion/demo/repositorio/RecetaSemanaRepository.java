package com.mutricion.demo.repositorio;

import com.mutricion.demo.modelo.RecetaSemana;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaSemanaRepository extends JpaRepository<RecetaSemana, Long>{
    RecetaSemana save(RecetaSemana recetaSemana);
}
