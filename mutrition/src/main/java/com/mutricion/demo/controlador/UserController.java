package com.mutricion.demo.controlador;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.Gson;
import com.mutricion.demo.configuracion.MessageConfig;
import com.mutricion.demo.modelo.Alergia;
import com.mutricion.demo.modelo.Role;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.modelo.UserForm;

import com.mutricion.demo.servicio.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private UserService userService;
    private MessageConfig messageConfig;
    private RestTemplate restTemplate;
    Gson gson;

   /* @Autowired
    private AlergiaService alergiaService;*/
    
    public UserController(UserService userService,
                MessageConfig messageConfig, RestTemplate restTemplate){
            this.userService=userService;
            this.restTemplate=restTemplate;
            this.messageConfig=messageConfig;
            gson=new Gson();
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(UserForm userParser, BindingResult bindingResult) throws UnsupportedEncodingException{
       
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ModelAndView modelAndView = new ModelAndView();

        JSONObject userJson=new JSONObject();
        userJson.put("password",  passwordEncoder.encode(userParser.getPassword()));//encryptar
        userJson.put("name", userParser.getName());
        System.out.println("el lastname es " + userParser.getLastname());
        userJson.put("lastname", userParser.getLastname());
        userJson.put("email", userParser.getEmail());
        userJson.put("sexo", userParser.getSexo());
        userJson.put("peso", userParser.getPeso());
        userJson.put("altura", userParser.getAltura());
        userJson.put("alergias", userParser.getAlergiasStr());
        userJson.put("roles","2");
       
        String query = userJson.toString();
        String uri = "http://localhost:1880/addUser/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        String body = restTemplate.postForObject(uri, entity, String.class);
        
        JSONObject bodyObject = new JSONObject(body);

        if (bodyObject.getString("statusType").equals("OK")){
            modelAndView.addObject("successMessage","Usuario registrado correctamente");
            modelAndView.addObject("user", new UserForm());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            //User user = userService.findUserByEmail(auth.getName());
            User user = userService.findUserByEmail(userParser.getEmail());
            //userService.updateUser2FA(true, user);
            modelAndView.addObject("qr", userService.generateQRUrl(user));
            modelAndView.setViewName("qr");

        } else if (bodyObject.getString("statusType").equals("NOT_ACCEPTABLE")){
            bindingResult.rejectValue("gmail", "error.user",
                    "*There is already a user registered with the provided email");
        }
        return modelAndView;
    }

    @GetMapping(value="/registrarse")
    public ModelAndView registrarse(){
        
        ModelAndView modelAndView = new ModelAndView();
        //get alergias
      
        String uri = "http://localhost:1880/getAlergias";

        String body = restTemplate.getForObject(uri, String.class);
        JSONObject bodyObject = new JSONObject(body);
        JSONArray lista = null;
        List<Alergia> alergias=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
               alergias.add((gson.fromJson(lista.getJSONObject(i).toString(), Alergia.class)));
           }
        }

       uri = "http://localhost:1880/getRoles";

       body = restTemplate.getForObject(uri, String.class);
       bodyObject = new JSONObject(body);
       List<Role> roles=new ArrayList<>();

       if(bodyObject.getString("statusType").equals("OK")){
           lista = bodyObject.getJSONArray("entity");
           for(int i = 0; i < lista.length(); i++) {
               roles.add((gson.fromJson(lista.getJSONObject(i).toString(), Role.class)));
           }
       }
       
       UserForm userParser = new UserForm();
       modelAndView.addObject("user", userParser);
     
       modelAndView.addObject("alergias", alergias);
       modelAndView.addObject("roles", roles);
       modelAndView.setViewName("registrarse");
       return modelAndView;
    }


    @GetMapping(value="/settings")
    public ModelAndView configuracion(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settings");
        return modelAndView;
    }

    @PostMapping(value = "/updateUserCuenta")
    public ModelAndView UpdateUser(UserForm userParser, BindingResult bindingResult) throws UnsupportedEncodingException{
       
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        JSONObject userJson=new JSONObject();
       
        userJson.put("name", userParser.getName());
        userJson.put("lastname", userParser.getLastname());
        //userJson.put("email", userParser.getEmail());
        
        int id = user.getId();
       
        String query = userJson.toString();
        String uri = "http://localhost:1880/updateUser/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        restTemplate.put(uri, entity, id);
        
        modelAndView.setViewName("settings");

      
        return modelAndView;
    }


}
