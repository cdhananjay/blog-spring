package com.example.blog.controllers;

import com.example.blog.domain.dtos.AuthResponseDto;
import com.example.blog.domain.dtos.LoginRequestDto;
import com.example.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );
        String token = authenticationService.generateToken(userDetails);
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .token(token)
                .expiresIn(24*60*60)
                .build();
        return ResponseEntity.ok(authResponseDto);
    }
}
