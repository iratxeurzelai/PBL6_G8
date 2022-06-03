package com.mutricion.demo.security.JWT;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.mutricion.demo.security.SecurityConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    @Autowired
    private JWTUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

            String header=null;
            Cookie [] cookies=req.getCookies();
            System.err.println("ANTES DEL IF DE LOS COOKIES");
            if(cookies!=null){
                System.err.println("DENTRO   DEL IF DE LOS COOKIES");
                for(Cookie cookie:cookies){
                    System.err.println("ANTES DEL FOR DEL IF DE LOS COOKIES");
                    if(cookie.getName().equals("JWT")){
                        header="Bearer "+cookie.getValue();
                        System.out.println("Obtiene cookie con value: "+header);
                        break;
                    }
                }
    
            } else if((header = req.getHeader(SecurityConstants.HEADER_STRING))
                        !=null && header.startsWith(SecurityConstants.TOKEN_PREFIX)){
    
                System.out.println("Obtiene header: "+header);
            } else {
                chain.doFilter(req, res);
                return;
            }
    
            if(header==null){
                chain.doFilter(req, res);
                return;
            }
            
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req, header);
    
            SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req, String token) {

        try{
       
            String user=JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getSubject();

            if (user != null) {

                Claim authoritiesClaim = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                                        .build()
                                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                                        .getClaim(SecurityConstants.ROLE_CLAIM);
                
                List<String> userRoles=authoritiesClaim.asList(String.class);
                List<SimpleGrantedAuthority> roles = new ArrayList<>();

                for (String role : userRoles) {
                    roles.add(new SimpleGrantedAuthority(role));
                }
                
                return new UsernamePasswordAuthenticationToken(user, null, roles);
            
            }
            return null;

        } catch (TokenExpiredException ex) {
           
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    null, null, null);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        return null;
    }
}