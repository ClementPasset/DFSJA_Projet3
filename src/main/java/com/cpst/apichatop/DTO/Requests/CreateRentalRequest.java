package com.cpst.apichatop.DTO.Requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateRentalRequest {
    private String name;
    private String description;
    private String surface;
    private String price;
    private MultipartFile picture;
}
