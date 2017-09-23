package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
// tells Spring Boot to drop its autoconfigured security policy and use this one instead
@EnableWebSecurity
// turns on method-level security with Spring Security’s sophisticated @Pre and @Post annotations
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SpringDataJpaUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(SpringDataJpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(Manager.PASSWORD_ENCODER);
    }

    // The paths listed in antMatchers() are granted unconditional access since there is no reason to block static web resources.
    // Anything that doesn’t match that falls into anyRequest().authenticated() meaning it requires authentication.
    // With those access rules setup, Spring Security is told to use form-based authentication, defaulting to "/" upon success, and to grant access to the login page.
    // BASIC login is also configured with CSRF disabled. This is mostly for demonstrations and not recommended for production systems without careful analysis.
    // Logout is configured to take the user to "/".
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/built/**", "/main.css").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/");
    }
}
