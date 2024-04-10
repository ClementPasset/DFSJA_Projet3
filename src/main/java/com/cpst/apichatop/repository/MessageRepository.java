package com.cpst.apichatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cpst.apichatop.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
