package com.zbartholomew.springsecurityandangularjs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class UiApplication {

    // Useful trick in a Spring Security application.
    // If the "/user" resource is reachable then it will return the
    // currently authenticated user (an Authentication),
    // and otherwise Spring Security will intercept the request and
    // send a 401 response through an AuthenticationEntryPoint
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

//    @RequestMapping("/resource")
//    public Map<String, Object> home() {
//        Map<String, Object> model = new HashMap<>();
//        model.put("id", UUID.randomUUID().toString());
//        model.put("content", "Hello World!");
//        return model;
//    }

    // Allowing anonymous access to the static (HTML) resources
    // (the CSS and JS resources are already accessible by default).
    // The HTML resources need to be available to anonymous users
    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }
}
