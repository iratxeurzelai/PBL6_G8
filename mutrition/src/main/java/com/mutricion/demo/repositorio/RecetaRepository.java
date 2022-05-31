package com.mutricion.demo.repositorio;

import com.mutricion.demo.modelo.Receta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long>{
    Receta findByTitle(String title);
    Receta findById(int id);
}
