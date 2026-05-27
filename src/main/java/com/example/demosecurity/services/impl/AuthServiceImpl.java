package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.LoginRequestDto;
import com.example.demosecurity.dtos.request.LogoutRequestDto;
import com.example.demosecurity.dtos.request.RefreshTokenRequest;
import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.AuthResponse;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.security.jwt.JwtService;
import com.example.demosecurity.services.AuthService;
import com.example.demosecurity.services.RefreshTokenService;
import com.example.demosecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public UserResponseDto register(UserRequestDto request) {
        return userService.createUser(request);
    }

    @Override
    public AuthResponse login(LoginRequestDto request) {

        // Delega la autenticación a Spring Security.
        //
        // El AuthenticationManager utilizará internamente:
        // - UserDetailsService
        // - PasswordEncoder
        // - AuthenticationProvider
        Authentication authentication = authenticationManager.authenticate(
                // Representa las credenciales enviadas por el usuario
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new IllegalStateException(
                    "Authentication principal is not an instance of UserDetails"
            );
        }

        // Se generan los tokens para el usuario autenticado.
        // El access token se usa para acceder a recursos protegidos.
        // El refresh token se guarda en base de datos y permite renovar el access token.
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public void logout(LogoutRequestDto requestDto) {
        refreshTokenService.revokeToken(requestDto.refreshToken());
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        String userEmail = refreshTokenService.getSubjectAndMarkAsUsed(request.refreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = refreshTokenService.generateRefreshToken(userEmail);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}