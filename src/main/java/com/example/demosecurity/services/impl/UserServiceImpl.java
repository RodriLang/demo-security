package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.enums.RoleType;
import com.example.demosecurity.mappers.UserMapper;
import com.example.demosecurity.models.Role;
import com.example.demosecurity.models.User;
import com.example.demosecurity.repositories.RoleRepository;
import com.example.demosecurity.repositories.UserRepository;
import com.example.demosecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        User user = userMapper.toEntity(request);
        // La contraseña se almacena hasheada utilizando BCrypt.
        // Nunca debe persistirse en texto plano.
        user.setPassword(passwordEncoder.encode(request.password()));


        // Buscamos los roles en la base de datos por su RoleType
        Set<Role> roles = request.roles()
                .stream()
                .map(roleType -> roleRepository.findByName(roleType)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleType)))
                .collect(Collectors.toSet());

        user.setRoles(roles);


        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}
