package com.cpst.apichatop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cpst.apichatop.model.DBUser;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {
    Optional<DBUser> findByEmail(String email);
}