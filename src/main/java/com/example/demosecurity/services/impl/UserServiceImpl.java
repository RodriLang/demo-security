package com.example.demosecurity.services.impl;

import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.enums.RoleType;
import com.example.demosecurity.exceptions.DuplicatedEntityException;
import com.example.demosecurity.mappers.UserMapper;
import com.example.demosecurity.models.RoleEntity;
import com.example.demosecurity.models.UserEntity;
import com.example.demosecurity.repositories.UserRepository;
import com.example.demosecurity.services.RoleService;
import com.example.demosecurity.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        validateUsername(request.username());

        UserEntity userEntity = userMapper.toEntity(request);
        // Seteamos la contraseña encriptada, nunca en texto plano
        userEntity.setPassword(passwordEncoder.encode(request.password()));


        // Buscamos el Role en la base de datos por su RoleType
        RoleEntity roleEntity = roleService.findByRoleName(RoleType.USER);
        // Asignamos el ROLE_USER por defecto
        userEntity.setRoles(Set.of(roleEntity));

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userMapper.toDto(savedUserEntity);
    }

    private void validateUsername(String username){
        if(userRepository.existsByUsername(username)){
            throw new DuplicatedEntityException("Ya existe un usuario registrado como " + username);
        }
    }
}
