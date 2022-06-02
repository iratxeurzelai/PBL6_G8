package com.mutricion.demo.servicio;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.mutricion.demo.configuracion.MessageConfig;
import com.mutricion.demo.modelo.ChangePasswordForm;
import com.mutricion.demo.modelo.Role;
import com.mutricion.demo.modelo.User;
import com.mutricion.demo.repositorio.RoleRepository;
import com.mutricion.demo.repositorio.UserRepository;

@Service
public class UserService {

    public static String APP_NAME = "mutrition";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    public UserService(UserRepository userRepository,RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        System.out.println(user);
        return userRepository.save(user);
    }

    public void cambiarContraseña(ChangePasswordForm form) throws Exception {
        User user = findUserById(form.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(form.getActPassword(), user.getPassword())) {
            throw new Exception(messageConfig.getMessage("error.password.invalid"));
        }
        if (passwordEncoder.matches(form.getNewPassword(), user.getPassword())) {
            throw new Exception(messageConfig.getMessage("error.password.same"));
        }
        user.setPassword(form.getNewPassword());
        saveUser(user);
    }

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public String generateQRUrl(User user) throws UnsupportedEncodingException {

        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME,
                user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

}