package com.mutricion.demo.servicio;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class RestTemplateService {
    
    @Autowired
    RestTemplate rTemplate;

    Gson gson;

    public void getter(){

    }
}
