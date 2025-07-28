package com.ayushbadoni5.leaveManagement.Controllers;

import com.ayushbadoni5.leaveManagement.Config.JWTTokenHelper;
import com.ayushbadoni5.leaveManagement.DTOs.LoginRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.LoginResponseDto;
import com.ayushbadoni5.leaveManagement.DTOs.RegisterRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.RegisterResponseDto;
import com.ayushbadoni5.leaveManagement.Entities.User;
import com.ayushbadoni5.leaveManagement.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private  AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody RegisterRequestDto request){
        RegisterResponseDto response = authService.registerUser(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            String token = jwtTokenHelper.generateToken(user);

            LoginResponseDto response =  LoginResponseDto.builder()
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","))
                    ).build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
