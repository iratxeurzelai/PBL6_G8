package com.mutricion.demo.repositorio;

import java.util.List;

import com.mutricion.demo.modelo.Alergia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Integer> {

    List<Alergia> findAll();
    Alergia findByDescripcion(String role);
    Alergia findById(int id);
}

