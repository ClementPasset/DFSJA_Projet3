package com.cpst.apichatop.service;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.repository.DBUserRepository;

@Service
public class DBUserService {

    @Autowired
    private DBUserRepository dbUserRepository;

    public Optional<DBUser> findByEmail(String email) {
        return dbUserRepository.findByEmail(email);
    }

    public DBUser saveDBUser(DBUser user){
        DBUser newUser = dbUserRepository.save(user);
        return newUser;
    }

    public String getEmailFromAuthentication(Principal user) {
        String email = "";
        if (user instanceof UsernamePasswordAuthenticationToken) {
            email = getemailFromUsernamePasswordAuthentication(user);
        } else if (user instanceof JwtAuthenticationToken) {
            email = getemailFromJwtAuthentication(user);
        }
        return email;
    }

    private String getemailFromUsernamePasswordAuthentication(Principal user) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
        if (token.isAuthenticated()) {
            User u = (User) token.getPrincipal();
            return u.getUsername();
        } else {
            return null;
        }
    }

    private String getemailFromJwtAuthentication(Principal user) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) user;
        if (token.isAuthenticated()) {
            return token.getTokenAttributes().get("sub").toString();
        } else {
            return null;
        }
    }
}
