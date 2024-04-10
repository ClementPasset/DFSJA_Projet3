package com.cpst.apichatop.service;

import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.Message;
import com.cpst.apichatop.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
}
