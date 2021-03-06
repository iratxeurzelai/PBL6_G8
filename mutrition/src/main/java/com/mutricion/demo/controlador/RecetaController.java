package com.mutricion.demo.controlador;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.gson.Gson;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.modelo.Alternativa;
import com.mutricion.demo.modelo.Finde;
import com.mutricion.demo.modelo.Receta;
import com.mutricion.demo.modelo.RecetaRequestModel;
import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.rabbitmq.RabbitMQConfig;
import com.mutricion.demo.servicio.AlternativaService;
import com.mutricion.demo.servicio.FindeService;
import com.mutricion.demo.servicio.RecetaSemanaService;
import com.mutricion.demo.servicio.RecetaService;
import com.mutricion.demo.servicio.UserService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RecetaController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AlternativaService alternativaService;

    @Autowired
    RecetaSemanaService recetaSemanaService;

    @Autowired
    RecetaService recetaService;

    @Autowired
    FindeService findeService;

    private UserService userService;

    private RestTemplate restTemplate;
    Gson gson;

    static String URL_GET_RECETA_SEMANA = "http://localhost:1880/getRecetaSemana/";

    static RecetaSemana primerPlato;
    static RecetaSemana segundoPlato;

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
    public ModelAndView recetaSemana(HttpServletRequest request){

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
            objectToSend.put("prefiere", user.getPreferencias());
            objectToSend.put("noprefiere", user.getNoPreferencias());
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME, RabbitMQConfig.ROUTING_KEY_PUESTA, objectToSend.toString());
            try {
                Thread.sleep(180000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value="/createAlternativa")
    public ModelAndView crearAlternativa(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        JSONObject objectToSend=new JSONObject();
        objectToSend.put("id", user.getId());
        objectToSend.put("peso", user.getPeso());
        objectToSend.put("altura", user.getAltura());
        objectToSend.put("alergias", user.getAlergias());
        objectToSend.put("prefiere", user.getPreferencias());
        objectToSend.put("noprefiere", user.getNoPreferencias());

        String uri = URL_GET_RECETA_SEMANA+ user.getId();
        
        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        
        JSONArray lista = null;
        List<RecetaSemana> recetaSemana=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
                recetaSemana.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
            }
        }

        for(RecetaSemana r : recetaSemana){
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth()+1;
            
            objectToSend.put("dia", dia);
            objectToSend.put("mes", mes);

            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (r.isPrimer_plato())){
                objectToSend.put("itemid", r.getReceta().getId());

                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME2, RabbitMQConfig.ROUTING_KEY_DAR, objectToSend.toString());
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!r.isPrimer_plato())){
                objectToSend.put("itemid", r.getReceta().getId());
                
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME2, RabbitMQConfig.ROUTING_KEY_DAR, objectToSend.toString());
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        modelAndView.addObject("alternativaPrimero", new Receta());
        modelAndView.addObject("alternativaSegundo", new Receta());
        modelAndView.setViewName("userVip/indexVIP");
        return modelAndView;
    }

    @GetMapping(value = "/visualizarDietaSemana")
    public ModelAndView visualizarRecetaSemana(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = URL_GET_RECETA_SEMANA + id;

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
    public ModelAndView recetaDia(HttpServletRequest request){
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        
        String uri = URL_GET_RECETA_SEMANA+ user.getId();
        
        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        
        JSONArray lista;
        List<RecetaSemana> recetaSemana=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
                recetaSemana.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
            }
        }

        modelAndView = getPlatosDias(modelAndView, recetaSemana);
        
        List<Alternativa> alternativas = alternativaService.findByUser(user);
        List<Alternativa> alternativasPrimero = new ArrayList<>();
        List<Alternativa> alternativasSegundo = new ArrayList<>();

        for(Alternativa a:alternativas){
            int dia = a.getFecha().getDate();
            int mes = a.getFecha().getMonth();

            if(!(a.getReceta().getTitle().equals(primerPlato.getReceta().getTitle())) && !(a.getReceta().getTitle().equals(segundoPlato.getReceta().getTitle()))){
                if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue())){
                    if(a.isPrimero()){
                        alternativasPrimero.add(a);
                    }
                    else{
                        alternativasSegundo.add(a);
                    }
                }
            }
        }

        modelAndView.addObject("alternativasPrimero", alternativasPrimero);
        modelAndView.addObject("alternativasSegundo", alternativasSegundo);
        modelAndView.addObject("alternativaPrimero", new Receta());
        modelAndView.addObject("alternativaSegundo", new Receta());
        modelAndView.setViewName("userVip/indexVIP");
        return modelAndView;
    }

    @PostMapping(value="/cambiarRecetaPrimero")
    public ModelAndView cambiarRecetaPrimero(@Valid RecetaRequestModel receta){
        ModelAndView modelAndView = new ModelAndView();

        Receta recetaNueva = recetaService.findByTitle(receta.getTitle());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        RecetaSemana recetaACambiar = null;

        String uri = URL_GET_RECETA_SEMANA+ user.getId();
        
        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        
        JSONArray lista = null;
        List<RecetaSemana> recetaSemana=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
                recetaSemana.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
            }
        }
        for(RecetaSemana r : recetaSemana){
            
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth()+1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (r.isPrimer_plato())){

            recetaACambiar = r;
            }

            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!r.isPrimer_plato())){

                segundoPlato = r;
            }
        }

        recetaACambiar.setReceta(recetaNueva);
        recetaSemanaService.saveRecetaSemana(recetaACambiar);
        
        List<Alternativa> alternativas = alternativaService.findByUser(user);
        List<Alternativa> alternativasPrimero = new ArrayList<>();
        List<Alternativa> alternativasSegundo = new ArrayList<>();

        for(Alternativa a:alternativas){
            int dia = a.getFecha().getDate();
            int mes = a.getFecha().getMonth();

            if(!(a.getReceta().getTitle().equals(receta.getTitle()) && !(segundoPlato.getReceta().getTitle().equals(a.getReceta().getTitle())))){
                if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue())){
                    if(a.isPrimero()){
                        alternativasPrimero.add(a);
                    }
                    else{
                        alternativasSegundo.add(a);
                    }
                }
            }
        }

        modelAndView.addObject("primerPlato", receta.getTitle());
        modelAndView.addObject("segundoPlato", segundoPlato.getReceta().getTitle());
        modelAndView.addObject("alternativasPrimero", alternativasPrimero);
        modelAndView.addObject("alternativasSegundo", alternativasSegundo);
        modelAndView.addObject("alternativaPrimero", new RecetaRequestModel());
        modelAndView.addObject("alternativaSegundo", new RecetaRequestModel());
        modelAndView.setViewName("userVip/indexVIP");
        return modelAndView;
    }

    @PostMapping(value="/cambiarRecetaSegundo")
    public ModelAndView cambiarRecetaSegundo(@Valid RecetaRequestModel recetaDTO){
        ModelAndView modelAndView = new ModelAndView();

        Receta receta = new Receta(recetaDTO);
        Receta recetaNueva = recetaService.findByTitle(receta.getTitle());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        RecetaSemana recetaACambiar = null;

        String uri = URL_GET_RECETA_SEMANA+ user.getId();
        
        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        
        JSONArray lista = null;
        List<RecetaSemana> recetaSemana=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
                recetaSemana.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
            }
        }
        for(RecetaSemana r : recetaSemana){
            
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth()+1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!r.isPrimer_plato())){

                recetaACambiar = r;
            }

            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (r.isPrimer_plato())){
            
                primerPlato = r;
            }
        }

        recetaACambiar.setReceta(recetaNueva);
        recetaSemanaService.saveRecetaSemana(recetaACambiar);
        
        List<Alternativa> alternativas = alternativaService.findByUser(user);
        List<Alternativa> alternativasPrimero = new ArrayList<>();
        List<Alternativa> alternativasSegundo = new ArrayList<>();

        for(Alternativa a:alternativas){
            int dia = a.getFecha().getDate();
            int mes = a.getFecha().getMonth();
            if(!(a.getReceta().getTitle().equals(receta.getTitle()) && !(primerPlato.getReceta().getTitle().equals(a.getReceta().getTitle())))){
                if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue())){
                    if(a.isPrimero()){
                        alternativasPrimero.add(a);
                    }
                    else{
                        alternativasSegundo.add(a);
                    }
                }
            }
        }

        modelAndView.addObject("primerPlato", primerPlato.getReceta().getTitle());
        modelAndView.addObject("segundoPlato", receta.getTitle());
        modelAndView.addObject("alternativasPrimero", alternativasPrimero);
        modelAndView.addObject("alternativasSegundo", alternativasSegundo);
        modelAndView.addObject("alternativaPrimero", new Receta());
        modelAndView.addObject("alternativaSegundo", new Receta());
        modelAndView.setViewName("userVip/indexVIP");
        return modelAndView;
    }

    @GetMapping(value = "/finde")
    public ModelAndView finde(HttpServletRequest request){
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Finde> finde = findeService.findByUser(user);

        for(Finde f : finde){
        
            LocalDate date = LocalDate.now();
            DayOfWeek day = date.getDayOfWeek();
    
            int diaDeLaSemana = day.getValue();

            int sabadoSemana = Math.abs((Calendar.SATURDAY-1) - diaDeLaSemana);
            int domingoSemana = Math.abs((Calendar.SUNDAY+6) - diaDeLaSemana);
            int dia = f.getFecha().getDate();
            int mes = f.getFecha().getMonth() + 1;
            
            if((dia == LocalDate.now().getDayOfMonth()+sabadoSemana) && (mes == LocalDate.now().getMonthValue())){
                modelAndView.addObject("sabado", f.getReceta().getTitle());
            }
            if((dia == LocalDate.now().getDayOfMonth()+domingoSemana) && (mes == LocalDate.now().getMonthValue())){

                modelAndView.addObject("domingo", f.getReceta().getTitle());
            }
        }

        modelAndView.setViewName("userVip/recomendacionFinde");
        return modelAndView;
    }

    public ModelAndView getPlatosDias(ModelAndView modelAndView, List<RecetaSemana> recetaSemana){
        for(RecetaSemana receta : recetaSemana){
            
            int dia = receta.getFecha().getDate();
            int mes = receta.getFecha().getMonth() +1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (receta.isPrimer_plato())){

            primerPlato = receta;
            modelAndView.addObject("primerPlato", receta.getReceta().getTitle());
            }
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!receta.isPrimer_plato())){
            
            segundoPlato = receta;
            modelAndView.addObject("segundoPlato", receta.getReceta().getTitle());
            }
        }
        return modelAndView;
    }

    @GetMapping(value = "/home")
    public ModelAndView home(HttpServletRequest request){
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = URL_GET_RECETA_SEMANA + id;

        String body = restTemplate.getForObject(uri, String.class);
        
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<RecetaSemana> recetas=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
                recetas.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
            }

            JSONObject objectToSend=new JSONObject();
            objectToSend.put("id", user.getId());
            objectToSend.put("peso", user.getPeso());
            objectToSend.put("altura", user.getAltura());
            objectToSend.put("alergias", user.getAlergias());
            objectToSend.put("prefiere", user.getPreferencias());
            objectToSend.put("noprefiere", user.getNoPreferencias());

            modelAndView = getPlatosDias(modelAndView, recetas);
        }
        
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value = "/historial")
    public ModelAndView historial(HttpServletRequest request){
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int id = user.getId();

        String uri = URL_GET_RECETA_SEMANA + id;

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
