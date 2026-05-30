package com.example.demosecurity.security;

import com.example.demosecurity.models.UserEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Esta clase funciona como un Adaptador
 * entre la entidad User de la aplicación
 * y el modelo de autenticación de Spring Security.
*/

@NullMarked// Declara @NonNull todos los atributos de la clase
public record CustomUserDetails(UserEntity user) implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    public Long getId() {
        return user.getId();
    }

    // Spring Security trabaja con authorities.
    // Por convención los roles utilizan el prefijo ROLE_
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(
                        ROLE_PREFIX + role.getRoleName().name()
                ))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Los demás métodos de la interface utilizan
    // la implementación por defecto siendo que
    // nuestra entidad no tiene esos atributos
}