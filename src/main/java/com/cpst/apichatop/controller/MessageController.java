package com.cpst.apichatop.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.DTO.Requests.MessageRequest;
import com.cpst.apichatop.service.MessageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody MessageRequest messageRequest, Principal principal) {

        try {
            return ResponseEntity.ok(messageService.createMessage(messageRequest, principal));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.badRequest().body(exception);
        }

    }
}
