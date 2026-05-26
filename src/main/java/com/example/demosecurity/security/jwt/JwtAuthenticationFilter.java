package com.example.demosecurity.security.jwt;

import com.example.demosecurity.exceptions.response.ErrorResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene el header Authorization de la request
        final String authHeader = request.getHeader("Authorization");

        // Si no hay Bearer token, se continúa la cadena de filtros.
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

            // Extrae el username desde el token.
            // Si el token expiró o es inválido se devuelve 401.
            username = jwtService.extractUsername(jwt);

        } catch (ExpiredJwtException e) {

            writeUnauthorizedResponse(
                    response,
                    "El token expiró. Inicie sesión nuevamente."
            );
            return;

        } catch (Exception e) {

            writeUnauthorizedResponse(response, "Token inválido.");
            return;
        }

        // Obtiene la autenticación actual del contexto de seguridad.
        // Si ya existe una autenticación, significa que otro filtro
        // autenticó previamente al usuario.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Solo se autentica al usuario si:
        // 1. El token contiene un username válido
        // 2. No existe una autenticación previa
        if (username != null && authentication == null) {

            try {

                // Se obtiene el usuario desde la base de datos para validar:
                // - que el usuario siga existiendo
                // - que su información esté actualizada
                // - y recuperar sus authorities/roles
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Verifica firma, expiración y pertenencia del token
                if (!jwtService.isTokenValid(jwt, userDetails)) {

                    writeUnauthorizedResponse(response, "Token inválido.");
                    return;
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

                writeUnauthorizedResponse(response, "Token inválido.");
                return;
            }
        }

        // Continúa la ejecución de la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Construye manualmente una respuesta HTTP 401 Unauthorized
    // para devolver un error consistente en formato JSON.
    //
    // Se utiliza dentro del filtro porque en esta etapa todavía
    // no interviene el manejo global de excepciones de Spring
    // (@RestControllerAdvice / ExceptionHandler).
    private void writeUnauthorizedResponse(HttpServletResponse response,
                                           String message) throws IOException {

        // Código HTTP 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Se define que la respuesta será JSON codificado en UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Se arma una DTO de respuesta de error
        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .build();

        // Convierte el objeto Java a JSON y lo escribe en la respuesta HTTP
        response.getWriter().write(
                objectMapper.writeValueAsString(error)
        );
    }
}