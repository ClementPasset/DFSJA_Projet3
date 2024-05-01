package com.cpst.apichatop.service;

import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.Message;
import com.cpst.apichatop.repository.MessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {
    MessageRepository messageRepository;

    /**
     * Method to create a new message in database
     * 
     * @param message The message that should be saved in database
     * @return The message that has been created
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
}
