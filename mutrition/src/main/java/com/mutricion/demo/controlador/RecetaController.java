package com.mutricion.demo.controlador;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mutricion.demo.modelo.User;
import com.mutricion.demo.rabbitmq.RabbitMQConfig;
import com.mutricion.demo.servicio.UserService;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RecetaController {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    private UserService userService;
    
    @GetMapping(value="/dietaSemana")
    public ModelAndView configuracion(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/dietaSemana");
        return modelAndView;
    }

    @GetMapping(value = "/crearSemana")
    public ModelAndView postRecetaSemana(HttpServletRequest request) throws IOException {
        // guardar receta
        List<User> users = userService.findAll();
        
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
}
