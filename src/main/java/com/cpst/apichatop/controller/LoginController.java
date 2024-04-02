package com.cpst.apichatop.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.DBUserResponse;
import com.cpst.apichatop.repository.DBUserRepository;
import com.cpst.apichatop.repository.DBUserResponseRepository;
import com.cpst.apichatop.security.service.JWTService;

@RestController
public class LoginController {

    private JWTService jwtService;

    private DBUserRepository dbUserRepository;

    private DBUserResponseRepository dbUserResponseRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginController(JWTService jwtService,
            DBUserRepository dbUserRepository,
            DBUserResponseRepository dbUserResponseRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtService = jwtService;
        this.dbUserRepository = dbUserRepository;
        this.dbUserResponseRepository = dbUserResponseRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> getLogin(Authentication auth) {
        String token = jwtService.generateToken(auth);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody DBUser user) throws Exception {
        boolean userFound = dbUserRepository.findByEmail(user.getEmail()).isPresent();
        if (userFound) {
            return ResponseEntity.badRequest().body("User already exists.");
        } else {
            String password = user.getPassword();

            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setRole("USER");
            user.setCreatedAt(LocalDate.now());
            user.setUpdatedAt(LocalDate.now());

            dbUserRepository.save(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    password);

            return ResponseEntity.ok().body(jwtService.generateToken(auth));
        }
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getMe(Principal user) {
        String email = null;
        if (user instanceof UsernamePasswordAuthenticationToken) {
            email = getemailFromUsernamePasswordAuthentication(user);
        } else if (user instanceof JwtAuthenticationToken) {
            email = getemailFromJwtAuthentication(user);
        }
        if (email != null) {
            DBUserResponse userResponse = dbUserResponseRepository.findByEmail(email).get();
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
