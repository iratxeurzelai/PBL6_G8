package com.mutricion.demo.configuracion;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mutricion.demo.security.SecurityConstants;
import com.mutricion.demo.security.JWT.JWTUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            
        String token = jwtUtil.generateToken(authentication.getName(), (List<GrantedAuthority>) authentication.getAuthorities());
        
        String headerToken=SecurityConstants.TOKEN_PREFIX+token;

        logger.info("User: {} succesfully authenticated!", authentication.getName());

        System.out.println("El token es " + token);
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
       // cookie.setMaxAge((int) SecurityConstants.EXPIRATION_TIME);
        System.out.println("El cookie es " + cookie);
        System.out.println("-------------------------");
        response.addCookie(cookie);
        response.sendRedirect(SecurityConstants.SUCCESS_URL);
    }

    
}
