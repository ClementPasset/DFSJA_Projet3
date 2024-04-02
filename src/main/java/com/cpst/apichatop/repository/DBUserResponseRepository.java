package com.cpst.apichatop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cpst.apichatop.model.DBUserResponse;

public interface DBUserResponseRepository extends JpaRepository<DBUserResponse, Long> {
    public Optional<DBUserResponse> findByEmail(String email);
}
