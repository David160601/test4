package com.example.demo.product.dto;

import com.example.demo.validation.IdValidation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

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
    @IdValidation(message = "Brand id must not be empty")
    private long brandId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> categoryIds;
}
