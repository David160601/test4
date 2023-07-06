package com.example.demo.brand.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BrandDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    String title;
    String imgUrl;
}
