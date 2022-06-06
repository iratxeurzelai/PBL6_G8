package com.mutricion.demo.servicio;

import java.util.List;

import com.mutricion.demo.modelo.Alergia;
import com.mutricion.demo.repositorio.AlergiaRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AlergiaService {

    private AlergiaRepository alergiaRepository;

    @Autowired

    public AlergiaService(AlergiaRepository alergiaRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.alergiaRepository = alergiaRepository;
    }


    public List<Alergia> loadAlergias(){
      
        return alergiaRepository.findAll();
    }

    public Alergia buscarAlergiaId(int id){
        return alergiaRepository.findById(id);
    }

    public Alergia buscarRoleDescripcion(String descripcion){
        return alergiaRepository.findByDescripcion(descripcion);
    }


    public Alergia saveAlergia(Alergia alergia) {
        return alergiaRepository.save(alergia);
    }
}