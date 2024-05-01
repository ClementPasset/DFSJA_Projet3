package com.cpst.apichatop.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.Message;
import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.DTO.Requests.MessageRequest;
import com.cpst.apichatop.DTO.Responses.MessageResponse;
import com.cpst.apichatop.service.DBUserService;
import com.cpst.apichatop.service.MessageService;
import com.cpst.apichatop.service.RentalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;
    private DBUserService dbUserService;
    private RentalService rentalService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(
            @RequestBody MessageRequest messageRequest,
            Principal auth) {

        DBUser user = dbUserService.getUserInfo(auth);
        Rental rental = rentalService.getRentalById(messageRequest.getRental_id());

        if (user.getId() == messageRequest.getUser_id() && rental != null) {
            Message newMessage = new Message(messageRequest.getMessage(), rental, user);
            messageService.createMessage(newMessage);
            return ResponseEntity.ok(new MessageResponse("Message has been send."));
        } else {
            return ResponseEntity.badRequest().body("Incorrect input.");
        }
    }
}
