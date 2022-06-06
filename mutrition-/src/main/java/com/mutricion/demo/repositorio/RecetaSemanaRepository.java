package com.mutricion.demo.repositorio;

import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.modelo.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaSemanaRepository extends JpaRepository<RecetaSemana, Long>{
    RecetaSemana save(RecetaSemana recetaSemana);
    RecetaSemana findByrecetasemanaid(int recetasemanaid);
    List<RecetaSemana> findByuser(User user);
}
