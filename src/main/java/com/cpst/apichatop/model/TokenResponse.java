package com.cpst.apichatop.model;

import lombok.Data;

@Data
public class TokenResponse {
    public String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
