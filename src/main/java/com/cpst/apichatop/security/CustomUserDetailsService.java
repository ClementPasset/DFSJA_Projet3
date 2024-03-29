package com.cpst.apichatop.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.repository.DBUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DBUserRepository dbUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        DBUser user = dbUserRepository.findByEmail(email).get();
        return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles.split("\\|")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

}
