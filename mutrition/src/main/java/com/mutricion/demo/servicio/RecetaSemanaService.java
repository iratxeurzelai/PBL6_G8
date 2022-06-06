package com.mutricion.demo.servicio;

import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.repositorio.RecetaSemanaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecetaSemanaService {
    private RecetaSemanaRepository recetaSemanaRepository;

    @Autowired
    public RecetaSemanaService(RecetaSemanaRepository recetaSemanaRepository) {

        this.recetaSemanaRepository = recetaSemanaRepository;
    }

    public RecetaSemana saveRecetaSemana(RecetaSemana recetaSemana) {
        return recetaSemanaRepository.save(recetaSemana);
    }

    public RecetaSemana findByRecetaSemanaId(int id){
        return recetaSemanaRepository.findByrecetasemanaid(id);
    }

    public List<RecetaSemana> findByUserId(User user){
        return recetaSemanaRepository.findByuser(user);
    }
}
