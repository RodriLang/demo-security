package com.example.demosecurity.mappers;

import com.example.demosecurity.dtos.request.UserRequestDto;
import com.example.demosecurity.dtos.response.UserResponseDto;
import com.example.demosecurity.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    //IMPORTANTE por seguridad ignorar el mapeo de la contraseña en texto plano
    @Mapping(target = "password", ignore = true)
    UserEntity toEntity (UserRequestDto dto);

    UserResponseDto toDto(UserEntity entity);
}
