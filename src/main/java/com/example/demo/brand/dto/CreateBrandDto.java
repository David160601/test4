package com.example.demo.brand.dto;

import com.example.demo.validation.ImageValidation;
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
    @ImageValidation
    MultipartFile image;
}
