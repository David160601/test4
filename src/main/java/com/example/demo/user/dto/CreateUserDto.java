package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class CreateUserDto {
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;
    @NotBlank
    String email;
    @NotBlank
    String password;
}
