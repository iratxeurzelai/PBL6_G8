package com.mutricion.demo.security.factor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 1L;

    private final String verificationCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        System.out.println("----------------------------------------------------------------------------------------");
        verificationCode = request.getParameter("code");
        System.out.println(verificationCode);
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}