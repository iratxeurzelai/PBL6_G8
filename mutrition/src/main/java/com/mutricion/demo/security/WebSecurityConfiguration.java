package com.mutricion.demo.security;


import com.mutricion.demo.configuracion.CustomAuthenticationSuccessHandler;
import com.mutricion.demo.configuracion.CustomLogoutSuccessHandler;
import com.mutricion.demo.security.JWT.JWTAuthorizationFilter;
import com.mutricion.demo.security.factor.CustomAuthenticationProvider;
import com.mutricion.demo.security.factor.CustomWebAuthenticationDetailsSource;
import com.mutricion.demo.servicio.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // add app config

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String LOGIN_URL = "/login";

        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()  // this disables session creation on Spring Security
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers(LOGIN_URL).permitAll()
            //.antMatchers("/home").hasAnyAuthority("USER","VIP")
            .antMatchers("/registration").permitAll()
            .antMatchers("/UserVip/**").hasAuthority("VIP")
            .antMatchers("/user/**").hasAnyAuthority("USER","VIP")
            .anyRequest().authenticated().and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .formLogin()
                .authenticationDetailsSource(authenticationDetailsSource)
                .loginPage(LOGIN_URL)
                .loginPage("/")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler())
                .usernameParameter("email")
                .passwordParameter("password")
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(myLogoutSuccessHandler()).and().exceptionHandling().accessDeniedPage("/403");
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler myLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/bootstrap/**", "/js/**", "/img/**",
                "/fonts/**");
    }

}
