package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.LoginRequestDto;
import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.LoginResponseDto;
import com.example.demosecurity.dtos.response.UserResponseDto;

public interface AuthService {

    UserResponseDto register(UserRequestDto request);

    LoginResponseDto login(LoginRequestDto request);

}