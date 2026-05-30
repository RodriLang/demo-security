package com.example.demosecurity.mappers;

import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.models.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "roleName", target = "role")
    RoleResponseDto toDto(RoleEntity entity);
}
