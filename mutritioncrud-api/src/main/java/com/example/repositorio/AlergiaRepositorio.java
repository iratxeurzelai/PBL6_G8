package com.example.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.modelo.Alergia;

@Repository
public interface AlergiaRepositorio extends JpaRepository<Alergia, Integer> {
    Alergia findByDescripcion(String alergia);
    Alergia findById(int id);
    List<Alergia> findAll();
}
