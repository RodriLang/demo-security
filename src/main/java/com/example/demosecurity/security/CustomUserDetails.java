package com.example.demosecurity.security;

import com.example.demosecurity.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    public Long getId() {
        return user.getId();
    }

    public Collection<String> getRoles() {
        return user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();
    }

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