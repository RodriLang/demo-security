package com.example.demosecurity.services.impl;
import com.example.demosecurity.dtos.request.LoginRequestDto;
import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.LoginResponseDto;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.security.jwt.JwtService;
import com.example.demosecurity.services.AuthService;
import com.example.demosecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public UserResponseDto register(UserRequestDto request){
        return userService.createUser(request);
    }

    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDto(token);
    }
}