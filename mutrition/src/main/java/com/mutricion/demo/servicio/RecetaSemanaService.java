package com.mutricion.demo.servicio;

import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.repositorio.RecetaSemanaRepository;

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
}
