package com.cpst.apichatop.DTO.Requests;

import lombok.Data;

@Data
public class UpdateRentalRequest {
    private String name;
    private String description;
    private String surface;
    private String price;
}
