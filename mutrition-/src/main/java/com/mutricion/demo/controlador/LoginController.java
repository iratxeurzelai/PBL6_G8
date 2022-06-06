package com.mutricion.demo.controlador;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mutricion.demo.modelo.RecetaSemana;
import com.mutricion.demo.modelo.User;

import com.mutricion.demo.servicio.UserService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    private RestTemplate restTemplate;
    Gson gson;

    public LoginController(RestTemplate restTemplate){
        
            this.restTemplate=restTemplate;
            gson=new Gson();
    }

    @GetMapping(value = { "/", "/login" })
    public ModelAndView login() {
    
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Autho login->home: "+auth);
        System.out.println("Autho login->home: "+auth.getName()+" with authority: "+auth.getAuthorities());
        ModelAndView modelAndView = new ModelAndView();

        if(!auth.getName().equals("anonymousUser")){
            User user = userService.findUserByEmail(auth.getName());
            int id = user.getId();
            String uri = "http://localhost:1880/getRecetaSemana/"+ id;
            System.out.println("rest :" +restTemplate);
            
            String body = restTemplate.getForObject(uri, String.class);
            System.out.println("body: " +body);
            JSONObject bodyObject = new JSONObject(body);
            
            JSONArray lista = null;
            List<RecetaSemana> recetaSemana=new ArrayList<>();

            if(bodyObject.getString("statusType").equals("OK")){
                lista = bodyObject.getJSONArray("entity");
                for(int i = 0; i < lista.length(); i++) {
                    recetaSemana.add((gson.fromJson(lista.getJSONObject(i).toString(), RecetaSemana.class)));
                }
            }
            modelAndView.addObject("recetaSemana", recetaSemana);
            modelAndView.setViewName("user/home");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /*@GetMapping(value = "/home")
    @ResponseBody
    public ModelAndView home(@RequestParam(required = false) String msg) {
        ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("user/home");
        return modelAndView;
    }*/

    
}
