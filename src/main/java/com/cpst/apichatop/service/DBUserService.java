package com.cpst.apichatop.service;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.repository.DBUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DBUserService {

    private DBUserRepository dbUserRepository;

    /**
     * Retrieves a user by his email
     * 
     * @param email the email used to search for a user
     * @return the DBUser found with the email param
     */
    public Optional<DBUser> findByEmail(String email) {
        return dbUserRepository.findByEmail(email);
    }

    /**
     * Saves a user to the database
     * 
     * @param user The DBUser that should be saved
     * @return The succesfully saved DBUser
     */
    public DBUser saveDBUser(DBUser user) {
        DBUser newUser = dbUserRepository.save(user);
        return newUser;
    }

    /**
     * Retrieves the information of a user as a DBUser object
     * 
     * @param auth authenticated user
     * @return DBUser object with information of the authenticated user
     */
    public DBUser getUserInfo(Principal auth) {
        String email = null;
        email = this.getEmailFromAuthentication(auth);

        DBUser user = dbUserRepository.findByEmail(email).get();

        return user;
    }

    /**
     * retrieves the email of a user from the current authenticated user
     * 
     * @param user The authenticated user
     * @return A string containing the currently authenticated user's email
     */
    public String getEmailFromAuthentication(Principal user) {
        String email = "";
        if (user instanceof UsernamePasswordAuthenticationToken) {
            email = getemailFromUsernamePasswordAuthentication(user);
        } else if (user instanceof JwtAuthenticationToken) {
            email = getemailFromJwtAuthentication(user);
        }
        return email;
    }

    /**
     * get the email of the user if the authentication method is Username/Password
     * 
     * @param user The authenticated user
     * @return The email of the authenticated user
     */
    private String getemailFromUsernamePasswordAuthentication(Principal user) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
        if (token.isAuthenticated()) {
            User u = (User) token.getPrincipal();
            return u.getUsername();
        } else {
            return null;
        }
    }

    /**
     * get the email of the user if the authentication method is JWT
     * 
     * @param user The authenticated user
     * @return The email of the authenticated user
     */
    private String getemailFromJwtAuthentication(Principal user) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) user;
        if (token.isAuthenticated()) {
            return token.getTokenAttributes().get("sub").toString();
        } else {
            return null;
        }
    }

    /**
     * Checks if the user exists in database
     * 
     * @param user DBUser to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(DBUser user) {
        return dbUserRepository.findById(user.getId()) != null;
    }
}
