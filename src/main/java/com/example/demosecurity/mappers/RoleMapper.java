package com.example.demosecurity.mappers;

import com.example.demosecurity.dtos.response.RoleResponseDto;
import com.example.demosecurity.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDto toDto(Role entity);
}
