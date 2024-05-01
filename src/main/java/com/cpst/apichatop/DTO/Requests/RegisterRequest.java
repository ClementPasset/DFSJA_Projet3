package com.cpst.apichatop.DTO.Requests;

import lombok.Data;

@Data
public class RegisterRequest {
    public String email;
    public String password;
    public String name;
}
