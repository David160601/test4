package com.example.demo.brand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateBrandDto {
    @NotBlank
    String title;
    @NotBlank
    String imgUrl;
}
