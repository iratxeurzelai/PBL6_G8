package com.mutricion.demo.repositorio;
import com.mutricion.demo.modelo.Finde;
import com.mutricion.demo.modelo.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindeRepository extends JpaRepository<Finde, Long>{
    Finde save(Finde finde);
    List<Finde> findByFecha(Date fecha);
    List<Finde> findByUser(User user);
}
