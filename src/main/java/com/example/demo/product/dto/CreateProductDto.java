package com.example.demo.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateProductDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private int price;
    @NotNull
    private int qty;

    private long brandId;
}
