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

    
    /** 
     * @return List<Alergia>
     */
    public List<Alergia> loadAlergias(){
       System.out.println("holaaaaaa");
        return alergiaRepository.findAll();
    }

    
    /** 
     * @param alergia
     * @return Alergia
     */
    public Alergia saveAlergia(Alergia alergia) {
        return alergiaRepository.save(alergia);
    }
}