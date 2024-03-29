package com.cpst.apichatop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.repository.DBUserRepository;

@Service
public class DBUserService {

    @Autowired
    private DBUserRepository dbUserRepository;

    public Optional<DBUser> findByEmail(String email) {
        return dbUserRepository.findByEmail(email);
    }

    public DBUser saveDBUser(DBUser user){
        DBUser newUser = dbUserRepository.save(user);
        return newUser;
    }
}
