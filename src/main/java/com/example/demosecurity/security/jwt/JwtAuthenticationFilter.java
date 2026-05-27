package com.example.demosecurity.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene el header Authorization de la request
        final String authHeader = request.getHeader("Authorization");

        // Si no hay Bearer accessToken, se continúa la cadena de filtros.
        // La request podrá seguir siendo procesada como pública o será
        // rechazada más adelante por Spring Security si el endpoint requiere autenticación.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el JWT eliminando el prefijo "Bearer"
        final String jwt = authHeader.substring(7);

        final String username;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("The access token has expired", e);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid access token", e);
        }

        // Obtiene la autenticación actual del contexto de seguridad.
        // Si ya existe una autenticación, significa que otro filtro
        // autenticó previamente al usuario.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Solo se autentica al usuario si:
        // 1. El accessToken contiene un username válido
        // 2. No existe una autenticación previa
        if (username != null && authentication == null) {

            try {

                // Se obtiene el usuario desde la base de datos para validar:
                // - que el usuario siga existiendo
                // - que su información esté actualizada
                // - y recuperar sus authorities/roles
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Verifica firma, expiración y pertenencia del accessToken
                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    throw new BadCredentialsException("Invalid access token");
                }

                // Representa un usuario autenticado dentro de Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Agrega detalles de la request actual
                // (IP, sesión, etc.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Registra la autenticación en el contexto de seguridad
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid access token", e);
            }
        }

        // Continúa la ejecución de la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Ya no necesitamos el metodo writeUnauthorizedResponse()
    // vamos a implementar la interface AuthenticationEntryPoint
    // para manejar las excepciones con la clase JwtAuthenticationEntryPoint
}