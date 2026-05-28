package com.example.demosecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     Esta clase configura la política CORS de la aplicación.
     CORS (Cross-Origin Resource Sharing) es un mecanismo de seguridad del navegador
     que controla qué aplicaciones frontend pueden realizar peticiones a nuestra API.
     La creamos porque normalmente el frontend y el backend
     se ejecutan en dominios o puertos distintos,
     por ejemplo:

     Frontend -> http:// localhost:4200
     Backend -> http:// localhost:8080

     Sin configuración CORS, el navegador bloquearía las peticiones por seguridad.

     En esta clase definimos:

     - qué orígenes pueden consumir la API
     - qué métodos HTTP están permitidos
     - qué headers pueden enviarse
     - si se permiten credenciales/tokens
     - cuánto tiempo el navegador cachea esta configuración

     Finalmente, registramos la configuración para que Spring Security
     la aplique a todos los endpoints de la aplicación.
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Frontends permitidos
        // Aca deben declararse las URL de desde donde se harán peticiones a la API
        // En un entorno local, será otro puerto de localhost
        // En un entorno de producción, será la URL donde esté alojado el front
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:5173"
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Headers permitidos
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        // Permite enviar Authorization headers
        config.setAllowCredentials(true);

        // Tiempo que el navegador cachea la configuración CORS
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}