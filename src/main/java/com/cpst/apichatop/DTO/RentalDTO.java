package com.cpst.apichatop.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RentalDTO {
    private Long id;

    private String name;

    private float surface;

    private float price;

    private String picture;

    private String description;

    private LocalDate created_at;

    private LocalDate updated_at;

    private Long owner_id;
}
