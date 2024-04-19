package com.cpst.apichatop.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.Message;
import com.cpst.apichatop.model.MessageRequest;
import com.cpst.apichatop.model.MessageResponse;
import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.service.DBUserService;
import com.cpst.apichatop.service.MessageService;
import com.cpst.apichatop.service.RentalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class MessageController {

    MessageService messageService;
    DBUserService dbUserService;
    RentalService rentalService;

    public MessageController(
            MessageService messageService,
            DBUserService dbUserService,
            RentalService rentalService) {
        this.messageService = messageService;
        this.dbUserService = dbUserService;
        this.rentalService = rentalService;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(
            @RequestBody MessageRequest messageRequest,
            Principal user) {
        Long userId = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(user)).get().getId();
        Optional<Rental> optionalRental = rentalService.getRentalById(messageRequest.getRental_id());

        if (userId == messageRequest.getUser_id() && optionalRental.isPresent()) {
            Message newMessage = new Message(messageRequest.getMessage(), messageRequest.getRental_id(),
                    messageRequest.getUser_id());
            messageService.createMessage(newMessage);
            return ResponseEntity.ok(new MessageResponse("Message has been send."));
        } else {
            return ResponseEntity.badRequest().body("Incorrect input.");
        }
    }
}
