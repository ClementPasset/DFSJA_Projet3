package com.cpst.apichatop.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.DBUserResponse;
import com.cpst.apichatop.repository.DBUserResponseRepository;
import com.cpst.apichatop.security.service.JWTService;
import com.cpst.apichatop.service.DBUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class LoginController {

    private JWTService jwtService;

    private DBUserService dbUserService;

    private DBUserResponseRepository dbUserResponseRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginController(JWTService jwtService,
            DBUserService dbUserService,
            DBUserResponseRepository dbUserResponseRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtService = jwtService;
        this.dbUserService = dbUserService;
        this.dbUserResponseRepository = dbUserResponseRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/login")
    public ResponseEntity<String> getLogin(Authentication auth) {
        String token = jwtService.generateToken(auth);
        return ResponseEntity.ok().body(token);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody DBUser user) throws Exception {
        boolean userFound = dbUserService.findByEmail(user.getEmail()).isPresent();
        if (userFound) {
            return ResponseEntity.badRequest().body("User already exists.");
        } else {
            String password = user.getPassword();

            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setRole("USER");
            user.setCreatedAt(LocalDate.now());
            user.setUpdatedAt(LocalDate.now());

            dbUserService.saveDBUser(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    password);

            return ResponseEntity.ok().body(jwtService.generateToken(auth));
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/me")
    public ResponseEntity<?> getMe(Principal user) {
        String email = null;

        email = dbUserService.getEmailFromAuthentication(user);

        if (email != null) {
            DBUserResponse userResponse = dbUserResponseRepository.findByEmail(email).get();
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
