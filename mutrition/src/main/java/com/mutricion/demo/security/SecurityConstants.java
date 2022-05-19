package com.mutricion.demo.security;

public class SecurityConstants {

    public static final String SECRET = "SECRET_KEY";
    //public static final long EXPIRATION_TIME = 10_000; // 15 mins
    public static final long EXPIRATION_TIME = 900_000*8; // 2h

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/createUser";
    public static final String USERNAME = "email";
    public static final String PASSWORD = "password";
    public static final String SUCCESS_URL = "/home";
    public static final String ROLE_CLAIM = "roles";
}