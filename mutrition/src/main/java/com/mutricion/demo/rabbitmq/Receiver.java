package com.mutricion.demo.rabbitmq;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.servicio.RecetaSemanaService;
import com.mutricion.demo.servicio.RecetaService;
import com.mutricion.demo.servicio.UserService;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class Receiver {
    public static final String RECEIVE_METHOD_NAME = "receiveMessage";
    

    @Autowired
    RestTemplate rTemplate;
    @Autowired
    RecetaService recetaService;
    @Autowired
    UserService userService;
    @Autowired
    RecetaSemanaService recetaSemanaService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public Receiver() {
    }

    public void receiveMessage(byte[] message) {
        try {
            parseoBytesRecetas(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseoBytesRecetas(byte[] body) throws IOException {
        JSONObject jsonArray = new JSONObject(new String(body));
        String[] semana = {"LunesP", "LunesS", "MartesP", "MartesS", "MiercolesP", "MiercolesS", "JuevesP", "JuevesS",
        "ViernesP", "ViernesS"};
        
        int j=0;

        String nombre = null;
        User user = userService.findUserById((int) jsonArray.get("usuario"));

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        boolean primer_plato = true;
        boolean finde = true;

        for (int i = 0; i < semana.length; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(semana[i]);
            nombre = jsonObject.getString("name");
            Receta receta = recetaService.findByTitle(nombre);
            
            if((j%2==0) && (i != 0)){
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                primer_plato = true;
            }
            
            j++;
            RecetaSemana recetaSemana=new RecetaSemana();

            recetaSemana.setReceta(receta);
            recetaSemana.setUser(user);
            recetaSemana.setFecha(date);
            recetaSemana.setPrimer_plato(primer_plato);

            recetaSemanaService.saveRecetaSemana(recetaSemana);

            primer_plato = false;

            JSONObject objectToSend=new JSONObject();
            objectToSend.put("id", user.getId());
            objectToSend.put("peso", user.getPeso());
            objectToSend.put("altura", user.getAltura());
            objectToSend.put("alergias", user.getAlergias());
            objectToSend.put("prefiere", user.getPreferencias());
            objectToSend.put("noprefiere", user.getNoPreferencias());

            if(finde){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME3, RabbitMQConfig.ROUTING_KEY_RECOMENDAR, objectToSend.toString());
                finde = false;
            }
        }
    }
}