package com.example.demosecurity.config;

import com.example.demosecurity.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Define la configuración principal de seguridad HTTP.
    // Spring Security construirá internamente la cadena de filtros
    // a partir de esta configuración.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                // Se deshabilita CSRF porque la aplicación utiliza autenticación stateless con JWT.
                // CSRF tiene sentido principalmente en aplicaciones basadas en sesión y cookies.
                .csrf(AbstractHttpConfigurer::disable)

                // Define las reglas de autorización para los endpoints
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // USER y ADMIN pueden ver juegos
                        .requestMatchers(HttpMethod.GET, "/api/games/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Solo ADMIN puede modificar juegos
                        .requestMatchers(HttpMethod.POST, "/api/games/**")
                        // hasRole("ADMIN") internamente busca la authority "ROLE_ADMIN"
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/games/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/games/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/games/**")
                        .hasRole("ADMIN")

                        // Solo ADMIN puede gestionar roles
                        .requestMatchers(HttpMethod.GET, "/api/roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/roles/**").hasRole("ADMIN")

                        // Cualquier endpoint no declarado explícitamente será rechazado.
                        // Esto aplica el principio de "deny by default".
                        .anyRequest().denyAll()
                )
                // Indica que Spring Security no utilizará sesiones HTTP.
                // Cada request deberá autenticarse nuevamente mediante JWT.
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(STATELESS))
                // Se registra el filtro JWT antes del filtro de autenticación
                // estándar de Spring Security.
                //
                // De esta forma el JWT será validado antes de que Spring
                // intente autenticar al usuario mediante username/password.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // AuthenticationProvider encargado de autenticar usuarios
    // utilizando UserDetailsService y PasswordEncoder.
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // Expone el AuthenticationManager de Spring para poder
    // utilizarlo manualmente, por ejemplo en el login.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Encoder utilizado para hashear y verificar contraseñas
    // utilizando el algoritmo BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
