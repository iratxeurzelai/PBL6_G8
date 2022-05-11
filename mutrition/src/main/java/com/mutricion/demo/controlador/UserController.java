package com.mutricion.demo.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
//import com.gpch.login.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import com.mutricion.demo.modelo.Role;

@Controller

public class UserController {
    @Autowired

    @GetMapping(value={"/user/shop/categories"})
    public ModelAndView shop(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/shop/categories");
        return modelAndView;
    }

    @GetMapping(value={"/user/profile/profile"})
    public ModelAndView profile(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/profile/profile");
        return modelAndView;
    }

    @GetMapping(value={"/user/activities/myActivities"})
    public ModelAndView myActivities(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/activities/myActivities");
        return modelAndView;
    }

    @GetMapping(value={"/user/activities/futureActivities"})
    public ModelAndView futureActivities(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/activities/futureActivities");
        return modelAndView;
    }

    

}
