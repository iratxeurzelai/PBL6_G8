package com.mutricion.demo.controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.rabbitmq.RabbitMQConfig;
import com.mutricion.demo.servicio.UserService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RecetaController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private UserService userService;

    private RestTemplate restTemplate;
    Gson gson;

    public RecetaController(UserService userService,
                 RestTemplate restTemplate){
            this.userService=userService;
            this.restTemplate=restTemplate;
           
            gson=new Gson();
    }
    
    @GetMapping(value="/dietaSemana")
    public ModelAndView configuracion(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/dietaSemana");
        return modelAndView;
    }

    @GetMapping(value = "/crearSemana")
    public ModelAndView RecetaSemana(HttpServletRequest request) throws IOException {

        String uri = "http://localhost:1880/getUsers";

        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<User> users=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
            users.add((gson.fromJson(lista.getJSONObject(i).toString(), User.class)));
           }
        }
       
        for(User user:users){
            JSONObject objectToSend=new JSONObject();
            objectToSend.put("id", user.getId());
            objectToSend.put("peso", user.getPeso());
            objectToSend.put("altura", user.getAltura());
            objectToSend.put("alergias", user.getAlergias());
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME, RabbitMQConfig.ROUTING_KEY_PUESTA, objectToSend.toString());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value = "/visualizarDietaSemana")
    public ModelAndView VisualizarRecetaSemana(HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = "http://localhost:1880/getRecetaSemana/" + id;

        String body = restTemplate.getForObject(uri, String.class);
        
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<RecetaSemana> recetas=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
            recetas.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
           }
           
           modelAndView.addObject("receta10", recetas.get(recetas.size() -1).getReceta().getTitle());
           modelAndView.addObject("receta9", recetas.get(recetas.size() -2).getReceta().getTitle());
           modelAndView.addObject("receta8", recetas.get(recetas.size() -3).getReceta().getTitle());
           modelAndView.addObject("receta7", recetas.get(recetas.size() -4).getReceta().getTitle());
           modelAndView.addObject("receta6", recetas.get(recetas.size() -5).getReceta().getTitle());
           modelAndView.addObject("receta5", recetas.get(recetas.size() -6).getReceta().getTitle());
           modelAndView.addObject("receta4", recetas.get(recetas.size() -7).getReceta().getTitle());
           modelAndView.addObject("receta3", recetas.get(recetas.size() -8).getReceta().getTitle());
           modelAndView.addObject("receta2", recetas.get(recetas.size() -9).getReceta().getTitle());
           modelAndView.addObject("receta1", recetas.get(recetas.size() -10).getReceta().getTitle());
           
        }
       
        modelAndView.setViewName("user/dietaSemana");
        return modelAndView;
    }

    @GetMapping(value = "/recetaDia")
    public ModelAndView RecetaDia(HttpServletRequest request) throws IOException {
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = "http://localhost:1880/getRecetaSemana/" + id;

        String body = restTemplate.getForObject(uri, String.class);
        
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<RecetaSemana> recetas=new ArrayList<>();
        int count = 0;

        if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
            recetas.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
           }
          
           for(RecetaSemana receta : recetas){
               
               int dia = receta.getFecha().getDate();
               int mes = receta.getFecha().getMonth() +1;
               
               if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (count == 0)){
                   
                modelAndView.addObject("primerPlato", receta.getReceta().getTitle());
                count++;
                continue;
               }
               if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (count == 1)){
                
                modelAndView.addObject("segundoPlato", receta.getReceta().getTitle());
                count =0;
               }
           }
        }

        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value = "/historial")
    public ModelAndView historial(HttpServletRequest request) throws IOException {
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = "http://localhost:1880/getRecetaSemana/" + id;

        String body = restTemplate.getForObject(uri, String.class);
        
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<RecetaSemana> recetas=new ArrayList<>();
        

        if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
            recetas.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
           }
          
           modelAndView.addObject("recetas", recetas);
        }

        modelAndView.setViewName("user/historial");
        return modelAndView;
    }
}
