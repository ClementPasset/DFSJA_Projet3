package com.cpst.apichatop.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.DTO.DBUserDTO;
import com.cpst.apichatop.DTO.Requests.AuthRequest;
import com.cpst.apichatop.DTO.Requests.RegisterRequest;
import com.cpst.apichatop.DTO.Responses.TokenResponse;
import com.cpst.apichatop.mapper.DBUserDTOMapper;
import com.cpst.apichatop.security.service.JWTService;
import com.cpst.apichatop.service.DBUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class LoginController {

    private JWTService jwtService;

    private DBUserService dbUserService;

    private AuthenticationManager authManager;

    private DBUserDTOMapper dbUserDTOMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/login")
    public ResponseEntity<?> getLogin(@RequestBody AuthRequest authRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        try {
            TokenResponse tokenResponse = jwtService.getTokenResponse(auth);
            return ResponseEntity.ok().body(tokenResponse);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws Exception {
        boolean userFound = dbUserService.findByEmail(request.getEmail()).isPresent();
        if (userFound) {
            return ResponseEntity.badRequest().body("User already exists.");
        } else {
            String password = request.getPassword();

            DBUser user = dbUserDTOMapper.toEntity(request);

            DBUser savedUser = dbUserService.saveDBUser(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    savedUser.getEmail(),
                    password);

            TokenResponse tokenResponse = new TokenResponse(jwtService.generateToken(auth));

            return ResponseEntity.ok().body(tokenResponse);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/me")
    public ResponseEntity<?> getMe(Principal user) {
        try {
            DBUserDTO dbUserDto = dbUserDTOMapper.toDto(dbUserService.getUserInfo(user));
            return ResponseEntity.ok().body(dbUserDto);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }
    }
}
