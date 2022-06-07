package com.mutricion.demo.servicio;

import java.util.List;

import com.mutricion.demo.modelo.Preferencia;
import com.mutricion.demo.repositorio.PreferenciasRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PreferenciasService {
    private PreferenciasRepositorio preferenciasRepository;

    @Autowired

    public PreferenciasService(PreferenciasRepositorio preferenciasRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.preferenciasRepository = preferenciasRepository;
    }


    public List<Preferencia> loadPreferencias(){
      
        return preferenciasRepository.findAll();
    }

    public Preferencia buscarPreferenciaId(int id){
        return preferenciasRepository.findById(id);
    }

    public Preferencia buscarPreferenciaDescripcion(String descripcion){
        return preferenciasRepository.findByDescripcion(descripcion);
    }


    public Preferencia saveAlergia(Preferencia preferencias) {
        return preferenciasRepository.save(preferencias);
    }
}
