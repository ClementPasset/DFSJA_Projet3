package com.cpst.apichatop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class DBUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String role;

    private String password;

    public DBUser() {
    }

    public DBUser(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
