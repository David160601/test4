package com.example.demo.category.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCategoryDto {
    @NotBlank
    String title;
    @NotBlank
    String imgUrl;
}
