package com.mutricion.demo.controlador;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.gson.Gson;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.modelo.Alternativa;
import com.mutricion.demo.modelo.Finde;
import com.mutricion.demo.modelo.Receta;
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

    RecetaSemana primerPlato;
    RecetaSemana segundoPlato;

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
            Set<String> prefiere = new HashSet<>();
            prefiere.add("carne");
            Set<String> noprefiere = new HashSet<>();
            JSONObject objectToSend=new JSONObject();
            objectToSend.put("id", user.getId());
            objectToSend.put("peso", user.getPeso());
            objectToSend.put("altura", user.getAltura());
            objectToSend.put("alergias", user.getAlergias());
            objectToSend.put("prefiere", prefiere);
            objectToSend.put("noprefiere", noprefiere);
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME, RabbitMQConfig.ROUTING_KEY_PUESTA, objectToSend.toString());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value="/createAlternativa")
    public ModelAndView crearAlternativa(HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        JSONObject objectToSend=new JSONObject();
        Set<String> prefiere = new HashSet<>();
        prefiere.add("carne");
        Set<String> noprefiere = new HashSet<>();
        objectToSend.put("id", user.getId());
        objectToSend.put("peso", user.getPeso());
        objectToSend.put("altura", user.getAltura());
        objectToSend.put("alergias", user.getAlergias());
        objectToSend.put("prefiere", prefiere);
        objectToSend.put("noprefiere", noprefiere);

        List<RecetaSemana> recetaSemanas = recetaSemanaService.findByUserId(user);
        for(RecetaSemana r : recetaSemanas){
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth() +1;
            
            objectToSend.put("dia", dia);
            objectToSend.put("mes", mes);

            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (r.isPrimer_plato())){
                objectToSend.put("itemid", r.getReceta().getId());

                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME2, RabbitMQConfig.ROUTING_KEY_DAR, objectToSend.toString());
            }
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!r.isPrimer_plato())){
                objectToSend.put("itemid", r.getReceta().getId());
    
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGER_NAME2, RabbitMQConfig.ROUTING_KEY_DAR, objectToSend.toString());
            }
        }
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

        List<RecetaSemana> recetasSemana = recetaSemanaService.findByUserId(user);
        System.err.println("Lista recetas "+recetasSemana);
        for(RecetaSemana receta : recetasSemana){
            int dia = receta.getFecha().getDate();
            int mes = receta.getFecha().getMonth() +1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (receta.isPrimer_plato())){
            
            primerPlato = receta;
            }
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!receta.isPrimer_plato())){

            segundoPlato = receta;
            }
        }
        
        List<Alternativa> alternativas = alternativaService.findByUser(user);
        List<Alternativa> alternativasPrimero = new ArrayList<>();
        List<Alternativa> alternativasSegundo = new ArrayList<>();
        for(Alternativa a:alternativas){
            int dia = a.getFecha().getDate();
            int mes = a.getFecha().getMonth() +1;

            if(((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()))){
                if(a.isPrimero()){
                    alternativasPrimero.add(a);
                }
                else{
                    alternativasSegundo.add(a);
                }
            }
        }

        modelAndView.addObject("primerPlato", primerPlato.getReceta().getTitle());
        modelAndView.addObject("segundoPlato", segundoPlato.getReceta().getTitle());
        modelAndView.addObject("alternativasPrimero", alternativasPrimero);
        modelAndView.addObject("alternativasSegundo", alternativasSegundo);
        modelAndView.addObject("alternativaPrimero", new Receta());
        modelAndView.addObject("alternativaSegundo", new Receta());
        modelAndView.setViewName("user/indexVIP");
        return modelAndView;
    }

    @PostMapping(value="/cambiarRecetaPrimero")
    public ModelAndView cambiarRecetaPrimero(@Valid Receta receta){
        ModelAndView modelAndView = new ModelAndView();

        Receta recetaNueva = recetaService.findByTitle(receta.getTitle());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        RecetaSemana recetaACambiar = null;

        List<RecetaSemana> recetaSemana = recetaSemanaService.findByUserId(user);
        for(RecetaSemana r : recetaSemana){
            
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth() +1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (r.isPrimer_plato())){

            recetaACambiar = r;
            break;
            }
        }

        recetaACambiar.setReceta(recetaNueva);
        recetaSemanaService.saveRecetaSemana(recetaACambiar);
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @PostMapping(value="/cambiarRecetaSegundo")
    public ModelAndView cambiarRecetaSegundo(@Valid Receta receta){
        ModelAndView modelAndView = new ModelAndView();

        Receta recetaNueva = recetaService.findByTitle(receta.getTitle());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        RecetaSemana recetaACambiar = null;

        List<RecetaSemana> recetaSemana = recetaSemanaService.findByUserId(user);
        for(RecetaSemana r : recetaSemana){
            
            int dia = r.getFecha().getDate();
            int mes = r.getFecha().getMonth() +1;
            
            if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!r.isPrimer_plato())){

            recetaACambiar = r;
            break;
            }
        }

        recetaACambiar.setReceta(recetaNueva);
        recetaSemanaService.saveRecetaSemana(recetaACambiar);
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping(value = "/finde")
    public ModelAndView finde(HttpServletRequest request) throws IOException {
        // mostrar receta
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Finde> finde = findeService.findByUser(user);

        for(Finde f : finde){
        
            LocalDate date = LocalDate.now();
            DayOfWeek day = date.getDayOfWeek();
    
            int diaDeLaSemana = day.getValue();
            if(diaDeLaSemana==0){
                diaDeLaSemana = 7;
            }
            int sabadoSemana = Math.abs((Calendar.SATURDAY-1) - diaDeLaSemana);
            int domingoSemana = Math.abs((Calendar.SUNDAY+6) - diaDeLaSemana);
            int dia = f.getFecha().getDate();
            System.err.println("Dia " + dia);
            int mes = f.getFecha().getMonth() + 1;
            
            if((dia == LocalDate.now().getDayOfMonth()+sabadoSemana) && (mes == LocalDate.now().getMonthValue())){

                modelAndView.addObject("sabado", f.getReceta().getTitle());
            }
            if((dia == LocalDate.now().getDayOfMonth()+domingoSemana) && (mes == LocalDate.now().getMonthValue())){

                modelAndView.addObject("domingo", f.getReceta().getTitle());
            }
        }

        modelAndView.setViewName("user/recomendacionFinde");
        return modelAndView;
    }

    @GetMapping(value = "/home")
    public ModelAndView home(HttpServletRequest request) throws IOException {
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

            JSONObject objectToSend=new JSONObject();
            Set<String> prefiere = new HashSet<>();
            prefiere.add("carne");
            Set<String> noprefiere = new HashSet<>();
            objectToSend.put("id", user.getId());
            objectToSend.put("peso", user.getPeso());
            objectToSend.put("altura", user.getAltura());
            objectToSend.put("alergias", user.getAlergias());
            objectToSend.put("prefiere", prefiere);
            objectToSend.put("noprefiere", noprefiere);
            
            for(RecetaSemana receta : recetas){
            
                int dia = receta.getFecha().getDate();
                int mes = receta.getFecha().getMonth() +1;
                
                if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (receta.isPrimer_plato())){

                modelAndView.addObject("primerPlato", receta.getReceta().getTitle());
                }
                if((dia == LocalDate.now().getDayOfMonth()) && (mes == LocalDate.now().getMonthValue()) && (!receta.isPrimer_plato())){

                modelAndView.addObject("segundoPlato", receta.getReceta().getTitle());
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
