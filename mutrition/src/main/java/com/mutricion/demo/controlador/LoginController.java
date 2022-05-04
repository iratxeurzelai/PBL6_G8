package com.mutricion.demo.controlador;

import com.mutricion.demo.modelo.Role;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.servicio.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        
        return modelAndView;
    }


    @GetMapping(value="/registration")
    public ModelAndView registration(){
       
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        System.out.println("ha entrado a post registratioooooon");
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

   /* @GetMapping(value="/user/menu/menu")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Set<Role> roles = user.getRoles();
        Role role = (Role) roles.toArray()[0];
        if(role.getId() == 1){
            System.out.println("mamboooooooooooooooooooooooooooooooooooooooooooo");

            modelAndView.setViewName("admin/incidenceList");
        }

        else{
            System.out.println("no mamboooooooooooooooooooooooooo");
            modelAndView.setViewName("user/menu/menu");
        }
        //modelAndView.addObject("username", "Welcome " + user.getUsername() + "/" + user.getName() + " " + user.getLastname() + " (" + user.getEmail() + ")");
        //modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        
        return modelAndView;
    }*/

    @GetMapping(value="/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Set<Role> roles = user.getRoles();
        Role role = (Role) roles.toArray()[0];
        if(role.getId() == 1){
            modelAndView.setViewName("admin/incidenceList");
        }

        else{
            modelAndView.setViewName("home");
        }
        //modelAndView.addObject("username", "Welcome " + user.getUsername() + "/" + user.getName() + " " + user.getLastname() + " (" + user.getEmail() + ")");
        //modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        
        return modelAndView;
    }
}
