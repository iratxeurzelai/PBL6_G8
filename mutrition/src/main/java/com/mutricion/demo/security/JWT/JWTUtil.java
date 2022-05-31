package com.mutricion.demo.security.JWT;

import java.util.ArrayList;

import java.util.Date;

import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.mutricion.demo.security.SecurityConstants;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JWTUtil {

	public String generateToken(String subject, List<GrantedAuthority> grantetAuthorities) {
        List<String> authoritiesStr=new ArrayList<>();

        for(GrantedAuthority authority: grantetAuthorities){
            authoritiesStr.add(authority.getAuthority());
        }
        
		return doGenerateToken(authoritiesStr, subject);
	}

	public String doGenerateToken(List<String> authorities, String subject) {
		return JWT.create()
                .withSubject(subject)
               
                .withClaim(SecurityConstants.ROLE_CLAIM, authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}

	public String getUsernameFromToken(String token) throws ExpiredJwtException{

		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                .build()
                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getSubject();
	}

	public List<SimpleGrantedAuthority> getRolesFromToken(String token) {

        Claim authoritiesClaim = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                                .build()
                                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                                .getClaim(SecurityConstants.ROLE_CLAIM);

        List<String> userRoles=authoritiesClaim.asList(String.class);
        
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (String role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
	}

}