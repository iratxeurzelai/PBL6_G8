package com.mutricion.demo.servicio;

import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.repositorio.RecetaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecetaService {

    RecetaRepository recetaRepository;

    @Autowired
    public RecetaService(RecetaRepository recetaRepository) {

        this.recetaRepository = recetaRepository;
    }

    public Receta findByTitle(String title){
        return recetaRepository.findByTitle(title);
    }

    public Receta findById(int id){
        return recetaRepository.findById(id);
    }
}
