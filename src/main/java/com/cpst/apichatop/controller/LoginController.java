package com.cpst.apichatop.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.DTO.Requests.AuthRequest;
import com.cpst.apichatop.DTO.Requests.RegisterRequest;
import com.cpst.apichatop.Exceptions.AlreadyExistsException;
import com.cpst.apichatop.service.DBUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class LoginController {

    private DBUserService dbUserService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/login")
    public ResponseEntity<?> getLogin(@RequestBody AuthRequest authRequest) {

        return ResponseEntity.ok().body(dbUserService.login(authRequest));

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws AlreadyExistsException {

        try {
            return ResponseEntity.ok().body(dbUserService.registerUser(request));
        } catch (AlreadyExistsException exception) {
            return ResponseEntity.badRequest().body(exception);
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/me")
    public ResponseEntity<?> getMe(Principal user) {

        try {
            return ResponseEntity.ok().body(dbUserService.getUserDtoInfo(user));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }
}
