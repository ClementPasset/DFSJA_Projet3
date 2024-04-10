package com.cpst.apichatop.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {

    public Message(
            String message,
            Long rentalId,
            Long userId) {
        this.message = message;
        this.rentalId = rentalId;
        this.userId = userId;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "rental_id")
    public Long rentalId;

    @Column(name = "user_id")
    public Long userId;

    public String message;

    @Column(name = "created_at")
    public LocalDate createdAt;

    @Column(name = "updated_at")
    public LocalDate updatedAt;
}
