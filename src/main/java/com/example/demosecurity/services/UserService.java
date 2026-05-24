package com.example.demosecurity.services;

import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.UserResponseDto;


public interface UserService {

    UserResponseDto createUser(UserRequestDto request);
}
