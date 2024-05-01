package com.cpst.apichatop.mapper;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cpst.apichatop.DTO.DBUserDTO;
import com.cpst.apichatop.DTO.Requests.RegisterRequest;
import com.cpst.apichatop.model.DBUser;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DBUserDTOMapper {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public DBUserDTO toDto(DBUser user) {
        DBUserDTO dbUserDto = new DBUserDTO();

        dbUserDto.setId(user.getId());
        dbUserDto.setEmail(user.getEmail());
        dbUserDto.setName(user.getName());
        dbUserDto.setCreated_at(user.getCreatedAt());
        dbUserDto.setUpdated_at(user.getUpdatedAt());

        return dbUserDto;
    }

    public DBUser toEntity(RegisterRequest request) {
        DBUser user = new DBUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        return user;
    }

}
