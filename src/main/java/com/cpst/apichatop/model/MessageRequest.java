package com.cpst.apichatop.model;

import lombok.Data;

@Data
public class MessageRequest {
    public String message;
    public Long user_id;
    public Long rental_id;
}
