package com.cpst.apichatop.DTO.Requests;

import lombok.Data;

@Data
public class AuthRequest {
    public String email;
    public String password;
}
