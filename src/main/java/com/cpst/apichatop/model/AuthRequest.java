package com.cpst.apichatop.model;

import lombok.Data;

@Data
public class AuthRequest {
    public String email;
    public String password;
}
