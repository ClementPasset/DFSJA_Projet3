package com.cpst.apichatop.service;

import java.security.Principal;

import org.springframework.stereotype.Service;

import com.cpst.apichatop.DTO.Requests.MessageRequest;
import com.cpst.apichatop.DTO.Responses.MessageResponse;
import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.Message;
import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.repository.MessageRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {
    MessageRepository messageRepository;
    DBUserService dbUserService;
    RentalService rentalService;

    /**
     * Create a message in database if user and rental exist
     * 
     * @param request MessageRequest
     * @return MessageResponse
     */
    public MessageResponse createMessage(MessageRequest request, Principal principal) {

        DBUser user = dbUserService.getUserInfo(principal);
        Rental rental = rentalService.getRentalById(request.getRental_id());

        if (user != null && rental != null) {
            Message newMessage = new Message(request.message, rental, user);
            messageRepository.save(newMessage);

            return new MessageResponse("Message has been created.");
        } else {
            throw new EntityNotFoundException();
        }

    }
}
