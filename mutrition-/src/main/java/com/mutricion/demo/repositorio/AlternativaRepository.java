package com.mutricion.demo.repositorio;

import org.springframework.stereotype.Repository;

import com.mutricion.demo.modelo.Alternativa;
import com.mutricion.demo.modelo.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AlternativaRepository extends JpaRepository<Alternativa, Long>{
    Alternativa save(Alternativa alternativa);
    List<Alternativa> findByFecha(Date fecha);
    List<Alternativa> findByUser(User user);
}
