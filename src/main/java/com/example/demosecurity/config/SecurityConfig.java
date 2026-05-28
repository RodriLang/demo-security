package com.example.demosecurity.config;

import com.example.demosecurity.security.handler.JwtAuthenticationEntryPoint;
import com.example.demosecurity.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Define la configuración principal de seguridad HTTP.
     * Spring Security construirá internamente la cadena de filtros
     * a partir de esta configuración.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   // Recibimos por parámetro una fuente de configuración CORS
                                                   CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                // Se deshabilita CSRF porque la aplicación utiliza autenticación stateless con JWT.
                // CSRF tiene sentido principalmente en aplicaciones basadas en sesión y cookies.
                .csrf(AbstractHttpConfigurer::disable)

                // Agregamos la configuración cors a nuestra cadena de filtros
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Hacemos una configuración general de acceso
                // Definimos las reglas específicas en los Controller
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Indica que Spring Security no utilizará sesiones HTTP.
                // Cada request deberá autenticarse nuevamente mediante JWT.
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(STATELESS)
                )
                // Agregamos un manejador de excepciones a la cadena de filtros
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Se registra el filtro JWT antes del filtro de autenticación
                // estándar de Spring Security.
                //
                // De esta forma el JWT será validado antes de que Spring
                // intente autenticar al usuario mediante username/password.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    // AuthenticationProvider encargado de autenticar usuarios
    // utilizando UserDetailsService y PasswordEncoder.
    //
    // Spring Security puede configurar automáticamente un
    // DaoAuthenticationProvider si detecta un UserDetailsService
    // y un PasswordEncoder en el contexto.
    //
    // Se define explícitamente para tener mayor control y hacer
    // más visible la configuración del mecanismo de autenticación.
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
