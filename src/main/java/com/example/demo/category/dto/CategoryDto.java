package com.example.demo.category.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    String title;
    String imgUrl;
}
