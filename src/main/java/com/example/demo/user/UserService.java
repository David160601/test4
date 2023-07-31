package com.example.demo.user;

import com.example.demo.exception.ApiRequestException;
import com.example.demo.security.JwtService;
import com.example.demo.user.dto.CreateUserDto;
import com.example.demo.user.dto.LoginUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    public User getUserByEmail(String email) {
        return this.userRepo.findByEmail(email).orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));
    }

    public String registerUser(CreateUserDto createUserDto) {
        Optional<User> existUser = this.userRepo.findByEmail(createUserDto.getEmail());
        if (existUser.isPresent()) {
            throw new ApiRequestException("User already exist", HttpStatus.BAD_REQUEST);
        } else {
            User newUser = new User().builder().firstname(createUserDto.getFirstname())
                    .lastname(createUserDto.getLastname())
                    .email(createUserDto.getEmail())
                    .password(this.bCryptPasswordEncoder.encode(createUserDto.getPassword())).build();
            newUser = this.userRepo.save(newUser);
            String jwt = jwtService.generateToken(newUser);
            return jwt;
        }
    }

    public String loginUser(LoginUserDto loginUserDto) {
        User existUser = this.userRepo.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new ApiRequestException("Invalid username or password", HttpStatus.BAD_REQUEST));
        boolean passwordMatch = bCryptPasswordEncoder.matches(loginUserDto.getPassword(), existUser.getPassword());
        if (!passwordMatch) {
            throw new ApiRequestException("Invalid username or password", HttpStatus.BAD_REQUEST);
        } else {
            String jwt = jwtService.generateToken(existUser);
            return jwt;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserByEmail(username);
    }
}
