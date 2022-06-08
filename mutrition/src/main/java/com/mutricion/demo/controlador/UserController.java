package com.mutricion.demo.controlador;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.Gson;
import com.mutricion.demo.configuracion.MessageConfig;
import com.mutricion.demo.modelo.Alergia;
import com.mutricion.demo.modelo.ChangePasswordForm;
import com.mutricion.demo.modelo.Preferencia;
import com.mutricion.demo.modelo.Role;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.modelo.UserForm;
import com.mutricion.demo.servicio.AlergiaService;
import com.mutricion.demo.servicio.RoleService;
import com.mutricion.demo.servicio.UserService;

import groovyjarjarantlr4.v4.parse.ANTLRParser.id_return;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private UserService userService;

    MessageConfig messageConfig;
    
    private RestTemplate restTemplate;
    Gson gson;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AlergiaService alergiaService;
    
    public UserController(UserService userService,
                RestTemplate restTemplate, MessageConfig messageConfig){
            this.userService=userService;
            this.restTemplate=restTemplate;
            this.messageConfig = messageConfig;
            gson=new Gson();
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(UserForm userParser, BindingResult bindingResult) throws UnsupportedEncodingException{
    
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ModelAndView modelAndView = new ModelAndView();

        String alergias;
        if(userParser.getAlergiasStr() == null){
            alergias = "";
        }else{
            alergias = userParser.getAlergiasStr();
        }
        String prefiere;
        if(userParser.getPreferenciaStr() == null){
            prefiere = "";
        }else{
            prefiere = userParser.getPreferenciaStr();
        }
        String noprefiere;
        if(userParser.getNoPrefiereStr() == null){
            noprefiere = "";
        }else{
            noprefiere = userParser.getNoPrefiereStr();
        }

        JSONObject userJson=new JSONObject();
        userJson.put("password",  passwordEncoder.encode(userParser.getPassword()));//encryptar
        userJson.put("name", userParser.getName());
        userJson.put("lastname", userParser.getLastname());
        userJson.put("email", userParser.getEmail());
        userJson.put("sexo", userParser.getSexo());
        userJson.put("peso", userParser.getPeso());
        userJson.put("altura", userParser.getAltura());
        userJson.put("alergias", alergias);
        userJson.put("prefiere", prefiere);
        userJson.put("noprefiere", noprefiere);
        userJson.put("cuentaCorriente", "0");
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

            User user = userService.findUserByEmail(userParser.getEmail());
            
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
    
    uri = "http://localhost:1880/getPreferencias";

    body = restTemplate.getForObject(uri, String.class);
    bodyObject = new JSONObject(body);
    lista = null;
    List<Preferencia> preferencias=new ArrayList<>();

    if(bodyObject.getString("statusType").equals("OK")){
        lista = bodyObject.getJSONArray("entity");
        for(int i = 0; i < lista.length(); i++) {
            preferencias.add((gson.fromJson(lista.getJSONObject(i).toString(), Preferencia.class)));
        }
    }
    for(Preferencia p : preferencias){
        System.err.println("Descripcion " + p.getDescripcion());
    }

    UserForm userParser = new UserForm();
    modelAndView.addObject("user", userParser);
    modelAndView.addObject("preferencias", preferencias);
    modelAndView.addObject("alergias", alergias);
    modelAndView.addObject("roles", roles);
    modelAndView.setViewName("registrarse");
    return modelAndView;
    }


    @GetMapping(value="/settings")
    public ModelAndView configuracion(){
        ModelAndView modelAndView = new ModelAndView();
        
        String url = "http://localhost:1880/getAlergias";

        String body = restTemplate.getForObject(url, String.class);
        JSONObject bodyObject = new JSONObject(body);
        
        JSONArray lista = null;
        List<Alergia> alergias=new ArrayList<>();

        if(bodyObject.getString("statusType").equals("OK")){
            lista = bodyObject.getJSONArray("entity");
            for(int i = 0; i < lista.length(); i++) {
            alergias.add((gson.fromJson(lista.getJSONObject(i).toString(), Alergia.class)));
            }
        }
        
        modelAndView.addObject("alergias", alergias);
    
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
        String uri = "http://localhost:1880/updateUserCuenta/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        restTemplate.put(uri, entity, id);
        
        modelAndView.setViewName("settings");

        
        return modelAndView;
    }

    @PostMapping(value = "/updateUserVip")
    public ModelAndView UpdateUserToVip(UserForm userParser, BindingResult bindingResult, HttpSession session) throws UnsupportedEncodingException{
        
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        JSONObject userJson=new JSONObject();
        
        Set<Role> roles = user.getRoles();
        Role role = roleService.buscarRoleId(1);
        roles.add(role);
        
        userJson.put("roles",roles);
        userJson.put("cuentaCorriente", userParser.getCuentaCorriente());

        int id = user.getId();
        
        String query = userJson.toString();
        String uri = "http://localhost:1880/updateUserVip/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        restTemplate.put(uri, entity, id);
        
        session.setAttribute("role", "VIP");
        modelAndView.setViewName("settings");

    
        return modelAndView;
    }

    @PostMapping(value = "/updateUserDatosPer")
    public ModelAndView UpdateUserDatosPersonales(UserForm userParser, BindingResult bindingResult) throws UnsupportedEncodingException{
    
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        JSONObject userJson=new JSONObject();

        userJson.put("sexo", userParser.getSexo());
        userJson.put("peso", userParser.getPeso());
        userJson.put("altura", userParser.getAltura());
    

        String alergiasStr = userParser.getAlergiasStr();
        Set<Alergia> alergiasList=new HashSet<>();
        if(alergiasStr!=null){
            String [] alergiasData=alergiasStr.split("[,]");
            if(alergiasData.length>1){
                for(String a:alergiasData){
                    Alergia alergia=alergiaService.buscarAlergiaId(Integer. parseInt(a));
                    alergiasList.add(alergia);
                }
            } else alergiasList.add(alergiaService.buscarAlergiaId(Integer.parseInt(alergiasData[0])));
        }
        userJson.put("alergias", alergiasList);
        
        int id = user.getId();
        
        String query = userJson.toString();
        String uri = "http://localhost:1880/updateUserDatosPer/" + id;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(query, headers);

        restTemplate.put(uri, entity, id);
        
        modelAndView.setViewName("settings");

    
        return modelAndView;
    }

    @PostMapping(value = "/changePassword")
    public ModelAndView changePassword(ChangePasswordForm form, BindingResult bindingResult) throws Exception{

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(form.getActPassword(), user.getPassword())) {
            throw new Exception(messageConfig.getMessage("error.password.invalid"));
        }
        if (passwordEncoder.matches(form.getNewPassword(), user.getPassword())) {
            throw new Exception(messageConfig.getMessage("error.password.same"));
        }
        user.setPassword(form.getNewPassword());
        userService.saveUser(user);

        ModelAndView modelAndView = new ModelAndView();


        modelAndView.setViewName("settings");


        return modelAndView;
    }
}
