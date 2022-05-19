package com.mutricion.demo.controlador;

import com.mutricion.demo.servicio.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

   /* @GetMapping(value={"/", "/login"})
    public ModelAndView logiin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        
        return modelAndView;
    }*/

    @GetMapping(value = { "/", "/login" })
    public ModelAndView login() {
    
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Autho login->home: "+auth);
        System.out.println("Autho login->home: "+auth.getName()+" with authority: "+auth.getAuthorities());
        ModelAndView modelAndView = new ModelAndView();
        
        if(!auth.getName().equals("anonymousUser")){
            modelAndView.setViewName("home");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping(value = {"/back"})
    public ModelAndView back() {
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/home")
    @ResponseBody
    public ModelAndView home(@RequestParam(required = false) String msg) {
        ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("home");
        return modelAndView;
    }

   /* @GetMapping(value="/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Set<Role> roles = user.getRoles();
        Role role = (Role) roles.toArray()[0];
        if(role.getId() == 1){
            modelAndView.setViewName("userVip/home");
        }
        else{
            modelAndView.setViewName("user/home");
        }
        return modelAndView;
    }*/
}
