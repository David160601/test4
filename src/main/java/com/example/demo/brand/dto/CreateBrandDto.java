package com.example.demo.brand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateBrandDto {
    @NotBlank
    String title;
    MultipartFile img;
}
