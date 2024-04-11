package com.cpst.apichatop.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.AuthRequest;
import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.DBUserResponse;
import com.cpst.apichatop.model.TokenResponse;
import com.cpst.apichatop.repository.DBUserResponseRepository;
import com.cpst.apichatop.security.service.JWTService;
import com.cpst.apichatop.service.DBUserService;

@RestController
public class LoginController {

    private JWTService jwtService;

    private DBUserService dbUserService;

    private DBUserResponseRepository dbUserResponseRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AuthenticationManager authManager;

    public LoginController(JWTService jwtService,
            DBUserService dbUserService,
            DBUserResponseRepository dbUserResponseRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.dbUserService = dbUserService;
        this.dbUserResponseRepository = dbUserResponseRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authManager = authManager;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> getLogin(@RequestBody AuthRequest authRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        String token = jwtService.generateToken(auth);
        TokenResponse tokenResponse = new TokenResponse(token);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody DBUser user) throws Exception {
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

            TokenResponse tokenResponse = new TokenResponse(jwtService.generateToken(auth));

            return ResponseEntity.ok().body(tokenResponse);
        }
    }

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
