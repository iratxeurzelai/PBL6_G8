package com.mutricion.demo.servicio;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mutricion.demo.modelo.Alternativa;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.repositorio.AlternativaRepository;

@Service
public class AlternativaService {
private AlternativaRepository alternativaRepository;

    @Autowired
    public AlternativaService(AlternativaRepository alternativaRepository) {

        this.alternativaRepository = alternativaRepository;
    }

    public Alternativa saveAlternativa(Alternativa alternativa) {
        return alternativaRepository.save(alternativa);
    }

    public List<Alternativa> findByFecha(Date date) {
        return alternativaRepository.findByFecha(date);
    }

    public List<Alternativa> findByUser(User user) {
        return alternativaRepository.findByUser(user);
    }
}
