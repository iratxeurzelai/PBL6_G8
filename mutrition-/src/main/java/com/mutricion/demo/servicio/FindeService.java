package com.mutricion.demo.servicio;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mutricion.demo.modelo.Finde;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.repositorio.FindeRepository;

@Service
public class FindeService {
private FindeRepository findeRepository;

    @Autowired
    public FindeService(FindeRepository findeRepository) {

        this.findeRepository = findeRepository;
    }

    public Finde saveFinde(Finde finde) {
        return findeRepository.save(finde);
    }

    public List<Finde> findByFecha(Date date) {
        return findeRepository.findByFecha(date);
    }

    public List<Finde> findByUser(User user) {
        return findeRepository.findByUser(user);
    }
}
