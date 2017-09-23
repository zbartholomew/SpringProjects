package com.zbartholomew.springdatarestdemo.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SpringDataJpaUserDetailsService implements UserDetailsService {

    private final ManagerRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(ManagerRepository repository) {
        this.repository = repository;
    }

    /**
     * Uses custom finder {@link ManagerRepository} findByName().
     * It then populates a Spring Security User instance, which implements the UserDetails interface.
     * Using Spring Securiy’s AuthorityUtils to transition from an array of string-based roles into a
     * Java List of GrantedAuthority.
     * @param name - username
     * @return UserDetails object so Spring Security can interrogate the user’s information
     * @throws UsernameNotFoundException - no user name found
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Manager manager = this.repository.findByName(name);
        return new User(manager.getName(), manager.getPassword(),
                AuthorityUtils.createAuthorityList(manager.getRoles()));
    }
}
