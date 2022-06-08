package com.mutricion.demo.rabbitmq;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.web.client.RestTemplate;

import com.mutricion.demo.modelo.Finde;
import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.servicio.FindeService;
import com.mutricion.demo.servicio.RecetaService;
import com.mutricion.demo.servicio.UserService;

public class Receiver3 {
    public static final String RECEIVE_METHOD_NAME3 = "receiveMessage3";

    @Autowired
    RestTemplate rTemplate;
    @Autowired
    RecetaService recetaService;
    @Autowired
    UserService userService;
    @Autowired
    FindeService findeService;

    public Receiver3(){
    }
    
    public void receiveMessage3(byte[] message) {
        try{
            guardarRecomendaciones(message);
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void guardarRecomendaciones(byte[] body) throws IOException, JSONException, ParseException{
        JSONObject jsonArray = new JSONObject(new String(body));
        String[] semana = {"Sabado", "Domingo"};
        User user = userService.findUserById(jsonArray.getInt("usuario"));

        Date fecha = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        for(int i=0; i<semana.length;i++){
            if(i == 0){
                c.add(Calendar.DATE, 8);
            }else{
                c.add(Calendar.DATE, 1);
            }
            fecha = c.getTime();

            int recetaid = jsonArray.getInt(semana[i]);
            Receta receta = recetaService.findById(recetaid);

            Finde finde = new Finde();
            finde.setReceta(receta);
            finde.setFecha(fecha);
            finde.setUser(user);

            if(receta!=null){
                findeService.saveFinde(finde);
            }
        }
    }
}
