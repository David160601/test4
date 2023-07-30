package com.example.demo.user;


import com.example.demo.user.dto.CreateUserDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    String registerUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return this.userService.registerUser(createUserDto);
    }
}
