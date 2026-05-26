package com.example.demosecurity.security;

import com.example.demosecurity.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// Adaptador entre la entidad User de la aplicación
// y el modelo de autenticación de Spring Security.
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

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
                        "ROLE_" + role.getName().name()
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
}