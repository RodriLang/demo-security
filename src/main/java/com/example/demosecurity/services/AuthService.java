package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.LoginRequestDto;
import com.example.demosecurity.dtos.request.LogoutRequestDto;
import com.example.demosecurity.dtos.request.RefreshTokenRequest;
import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.AuthResponse;
import com.example.demosecurity.dtos.response.UserResponseDto;

public interface AuthService {

    UserResponseDto register(UserRequestDto request);

    AuthResponse login(LoginRequestDto request);

    void logout(LogoutRequestDto requestDto);

    AuthResponse refresh(RefreshTokenRequest request);
}