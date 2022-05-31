package com.mutricion.demo.security.factor;


import com.mutricion.demo.modelo.User;
import com.mutricion.demo.repositorio.UserRepository;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

//@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        System.out.println("------------Ha entrado en el provider-----------------");
        String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
        System.out.println("verificationCode: " + verificationCode);
        System.out.println("authGetName: " + auth.getName());
        User user = userRepository.findByEmail(auth.getName());
        System.out.println(user);
        if ((user == null)) {
            System.out.println("El usuario no es el correcto");
            throw new BadCredentialsException("Invalid username or password");
        }
        //if (user.isUsing2FA()) {
            
            System.out.println(Base32.random());
            Totp totp = new Totp(user.getSecret());
            System.out.println(totp.verify(verificationCode));
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verfication code");
            }
       // }

        Authentication result = super.authenticate(auth);

        return new UsernamePasswordAuthenticationToken(user.getEmail(), result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
