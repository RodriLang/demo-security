package com.example.demosecurity.services.impl;
import com.example.demosecurity.dtos.request.LoginRequestDto;
import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.LoginResponseDto;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.security.CustomUserDetails;
import com.example.demosecurity.security.jwt.JwtService;
import com.example.demosecurity.services.AuthService;
import com.example.demosecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        // Obtiene el usuario autenticado retornado por Spring Security
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Se genera un JWT firmado para el usuario autenticado
        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDto(token);
    }
}