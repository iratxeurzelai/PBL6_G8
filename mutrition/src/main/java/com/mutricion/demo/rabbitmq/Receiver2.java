package com.mutricion.demo.rabbitmq;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


import com.mutricion.demo.modelo.Alternativa;
import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.servicio.AlternativaService;
import com.mutricion.demo.servicio.RecetaSemanaService;
import com.mutricion.demo.servicio.RecetaService;
import com.mutricion.demo.servicio.UserService;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class Receiver2 {
    public static final String RECEIVE_METHOD_NAME2 = "receiveMessage2";

    @Autowired
    RestTemplate rTemplate;
    @Autowired
    RecetaService recetaService;
    @Autowired
    UserService userService;
    @Autowired
    RecetaSemanaService recetaSemanaService;
    @Autowired
    AlternativaService alternativaService;

    public Receiver2(){
    }
    
    public void receiveMessage2(byte[] message) {
        try{
            cambiarReceta(message);
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void cambiarReceta(byte[] body) throws IOException, JSONException, ParseException{
        JSONObject jsonArray = new JSONObject(new String(body));

        User user = userService.findUserById(jsonArray.getInt("usuario"));

        int dia = jsonArray.getInt("dia");
        int mes = jsonArray.getInt("mes");

        Date fecha = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        fecha.setDate(dia);
        fecha.setMonth(mes);
        System.err.println("Fecha " + fecha);

        System.err.println("Body " + body);
        String nombre;

        for(int i=0; i<5;i++){
            JSONObject recetaJSON = jsonArray.getJSONObject(String.valueOf(i));
            System.err.println("Receta JSON " + recetaJSON + " i: " +i);
            nombre = recetaJSON.getString("name");
            System.err.println("Nombre " + nombre);
            Receta receta = recetaService.findByTitle(nombre);

            Alternativa alternativa = new Alternativa();
            alternativa.setReceta(receta);
            alternativa.setFecha(fecha);
            alternativa.setUser(user);
            if(Integer.valueOf(recetaJSON.getString("primer_plato")) == 1){
                alternativa.setPrimero(true);
            }else{
                alternativa.setPrimero(false);
            }
            alternativaService.saveAlternativa(alternativa);
        }
    }
}
